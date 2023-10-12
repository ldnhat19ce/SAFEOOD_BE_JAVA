package tech.dut.safefood.service.User;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import tech.dut.safefood.component.JwtTokenProvider;
import tech.dut.safefood.configurator.oauth.FacebookAccountDto;
import tech.dut.safefood.configurator.oauth.GoogleAccountDto;
import tech.dut.safefood.dto.SignUpUserDto;
import tech.dut.safefood.dto.UserProfileDto;
import tech.dut.safefood.dto.request.*;
import tech.dut.safefood.dto.response.UserAuthenticationResponseDto;
import tech.dut.safefood.enums.AuthProvider;
import tech.dut.safefood.enums.UserEnum;
import tech.dut.safefood.exception.SafeFoodException;
import tech.dut.safefood.model.SafeFoodUserPrincipal;
import tech.dut.safefood.model.User;
import tech.dut.safefood.model.UserInformation;
import tech.dut.safefood.repository.RoleRepository;
import tech.dut.safefood.repository.UserInformationRepository;
import tech.dut.safefood.repository.UserRepository;
import tech.dut.safefood.service.EmailSenderService;
import tech.dut.safefood.service.JwtUserdetailService;
import tech.dut.safefood.util.AppUtils;
import tech.dut.safefood.util.constants.Constants;

import javax.mail.internet.InternetAddress;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserAuthenticationService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserInformationRepository userInformationRepository;

    private final TemplateEngine templateEngine;

    private final EmailSenderService emailSenderService;


    @Value("${email.safefood.from}")
    private String SEND_MAIL_FROM;

    @Value("${email.safefood.time.plusMillis}")
    private Long TIME_PLUS;

    @Autowired
    protected RestTemplate restTemplate;

    @Autowired
    private AppUtils appUtils;

    @Transactional(rollbackFor = Exception.class)
    public UserAuthenticationResponseDto loginNormal(LoginRequestDto signInDTO) throws SafeFoodException {
        User user = this.userRepository.findByEmailAndProvider(signInDTO.getEmail(), AuthProvider.local)
                .orElseThrow(() -> new SafeFoodException(SafeFoodException.EMAIL_OR_PASSWORD_IS_INVALID));
        if (!user.getRole().getName().equals(Constants.ROLE_USER)) {
            throw new SafeFoodException(SafeFoodException.ERROR_USER_NOT_FOUND);
        }
        if (!passwordEncoder.matches(signInDTO.getPassword(), user.getPassword())) {
            throw new SafeFoodException(SafeFoodException.UNAUTHORIZED_INVALID_PASSWORD);
        }
        UserAuthenticationResponseDto userAuthenticationDTO = new UserAuthenticationResponseDto();
        if (UserEnum.Status.INACTIVE.equals(user.getStatus())) {
            userAuthenticationDTO.setDigitCodeExpired(user.getExpiredTime().toEpochMilli());
        }
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(SafeFoodUserPrincipal.create(user, JwtUserdetailService.getAuthorities(user)), user.getPassword(), JwtUserdetailService.getAuthorities(user));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String accessToken = tokenProvider.generateAccessToken(authentication, false);
        userAuthenticationDTO.setAccessToken(accessToken);
        userAuthenticationDTO.setTokenType("Bearer");
        userAuthenticationDTO.setExpired(tokenProvider.generateExpiredTime());
        userAuthenticationDTO.setRefreshToken(tokenProvider.generateAccessToken(authentication, false));
        userAuthenticationDTO.setUserId(user.getId());
        userAuthenticationDTO.setStatus(user.getStatus());

        if (null != signInDTO.getDeviceToken() && !signInDTO.getDeviceToken().isEmpty()) {
            handleUserSaveDeviceToken(user, signInDTO.getDeviceToken());
        }
        return userAuthenticationDTO;
    }

    @Transactional
    public UserAuthenticationResponseDto activateAccount(CodeRequestDto activateAccountDTO) throws SafeFoodException {
        User user = userRepository.findByResetToken(activateAccountDTO.getCode()).orElseThrow(
                () -> new SafeFoodException(SafeFoodException.DIGIT_CODE_INCORRECT)
        );
        if (user.getExpiredTime().isBefore(Instant.now())) {
            throw new SafeFoodException(SafeFoodException.DIGIT_CODE_HAS_EXPIRED);
        }
        user.setStatus(UserEnum.Status.ACTIVED);
        user.setResetToken(null);

        UserAuthenticationResponseDto userAuthenticationDTO = new UserAuthenticationResponseDto();

        Authentication authentication = new UsernamePasswordAuthenticationToken(SafeFoodUserPrincipal.create(user, JwtUserdetailService.getAuthorities(user)), null, null);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String accessToken = tokenProvider.generateAccessToken(authentication, false);
        userAuthenticationDTO.setAccessToken(accessToken);
        userAuthenticationDTO.setTokenType("Bearer");
        userAuthenticationDTO.setExpired(tokenProvider.generateExpiredTime());
        userAuthenticationDTO.setRefreshToken(tokenProvider.generateAccessToken(authentication, false));
        userAuthenticationDTO.setUserId(user.getId());
        userAuthenticationDTO.setStatus(user.getStatus());
        return userAuthenticationDTO;
    }


    @Transactional(rollbackFor = Exception.class)
    public void resendDigitCode(ResendDigitCodeRequestDto resendDigitCodeDTO) {
        User user = userRepository.findFirstByEmailAndStatus(resendDigitCodeDTO.getEmail(), UserEnum.Status.INACTIVE).orElseThrow(
                () -> new SafeFoodException(SafeFoodException.ERROR_USER_NOT_FOUND));

        String digitCode;
        do {
            digitCode = AppUtils.generateDigitCode();
        } while (userRepository.existsByResetToken(digitCode));
        user.setResetToken(digitCode);
        user.setExpiredTime(user.getExpiredTime().plusMillis(TIME_PLUS));
        this.sendDigitCodeToMail(resendDigitCodeDTO.getEmail().toLowerCase(), digitCode, Constants.ACTIVE_ACCOUNT_BY_CODE, Constants.TEMPLATE_ACTIVE_CODE);

    }

    public void sendDigitCodeToMail(String recipientEmail, String code, String subject, String template) throws SafeFoodException {
        try {
            Context context = new Context();
            Map<String, Object> model = new HashMap<>();
            model.put("code", code);
            context.setVariables(model);
            String content = templateEngine.process(template, context);
            emailSenderService.sendEmail(subject, new InternetAddress(SEND_MAIL_FROM, Constants.EMAIL_SEND_FROM_DA_PASS_NAME), recipientEmail, content);
        } catch (Exception e) {
            throw new SafeFoodException(SafeFoodException.ERROR_CAN_NOT_SEND_EMAIL);
        }
    }

    @Transactional(rollbackFor = SafeFoodException.class)
    public SignUpUserDto signUpUser(SignUpUserDto signUpUserDto) throws SafeFoodException {
        if (userRepository.existsByEmailIgnoreCaseAndProvider(signUpUserDto.getEmail(), AuthProvider.local)) {
            throw new SafeFoodException(SafeFoodException.ERROR_EMAIL_ALREADY_EXISTS);
        }
        if (null != signUpUserDto.getPhoneNumber() && userRepository.existsByPhoneNumberIgnoreCase(signUpUserDto.getPhoneNumber())) {
            throw new SafeFoodException(SafeFoodException.ERROR_PHONE_NUMBER_ALREADY_EXISTS);
        }
        if (this.checkPasswordInValid(signUpUserDto.getPassword())) {
            throw new SafeFoodException(SafeFoodException.PASSWORD_INPUT_IS_INVALID);
        }
        User user = new User();
        user.setPassword(passwordEncoder.encode(signUpUserDto.getPassword()));
        user.setEmail(signUpUserDto.getEmail().toLowerCase());
        if (null != signUpUserDto.getPhoneNumber()) {
            user.setPhoneNumber(signUpUserDto.getPhoneNumber());
        }
        user.setStatus(UserEnum.Status.INACTIVE);
        user.setProvider(AuthProvider.local);
        user.setRole(roleRepository.findFirstByName(Constants.ROLE_USER).orElse(null));

        String digitCode;
        do {
            digitCode = AppUtils.generateDigitCode();
        } while (userRepository.existsByResetToken(digitCode));

        this.sendDigitCodeToMail(signUpUserDto.getEmail().toLowerCase(), digitCode, Constants.ACTIVE_ACCOUNT_BY_CODE, Constants.TEMPLATE_ACTIVE_CODE);

        user.setResetToken(digitCode);
        user.setExpiredTime(Instant.now().plusMillis(TIME_PLUS));
        user = this.userRepository.save(user);
        UserInformation userInformation = new UserInformation();
        userInformation.setUser(user);
        userInformation.setFirstName(signUpUserDto.getFirstname());
        if (null != signUpUserDto.getBirthDay()) {
            userInformation.setBirthday(Instant.ofEpochMilli(signUpUserDto.getBirthDay()));
        }
        userInformationRepository.save(userInformation);

        signUpUserDto.setStatus(user.getStatus());
        return signUpUserDto;
    }

    private boolean checkPasswordInValid(String password) {
        return !password.matches(Constants.PASSWORD_PATTERN);
    }

    private Optional<User> getCurrentUser() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        return Optional.ofNullable(securityContext.getAuthentication()).map(authentication -> {
            User user = null;
            if (authentication.getPrincipal() instanceof UserDetails) {
                SafeFoodUserPrincipal userPrincipal = (SafeFoodUserPrincipal) authentication.getPrincipal();
                user = userRepository.findById(userPrincipal.getUserId()).get();
                return user;
            }
            return null;
        });
    }

    @Transactional(rollbackFor = Exception.class)
    public void updateAvatar(UserAvatarDto avatar) {
        User user = getCurrentUser().get();
        user.getUserInformation().setUserImage(avatar.getAvatar());
        userRepository.save(user);
    }

    @Transactional(rollbackFor = Exception.class)
    public void changePassword(ChangePasswordRequestDto changePasswordRequestDto) {
        User user = getCurrentUser().get();
        if (!passwordEncoder.matches(changePasswordRequestDto.getOldPassword(), user.getPassword())) {
            throw new SafeFoodException(SafeFoodException.ERROR_OLD_PASSWORD_NOT_MATCH);
        }
        if (checkPasswordInValid(changePasswordRequestDto.getNewPassword())) {
            throw new SafeFoodException(SafeFoodException.PASSWORD_INPUT_IS_INVALID);
        }
        if (!changePasswordRequestDto.getNewPassword().equals(changePasswordRequestDto.getConfirmNewPassword())) {
            throw new SafeFoodException(SafeFoodException.ERROR_CONFIRM_NEW_PASSWORD_NOT_MATCH);
        }
        user.setPassword(passwordEncoder.encode(changePasswordRequestDto.getNewPassword()));
        userRepository.save(user);
    }

    public void forgotPassword(ForgotPasswordRequestDto forgotPasswordDTO) {
        User user = userRepository.findByEmailAndProvider(forgotPasswordDTO.getEmail(), AuthProvider.local).orElseThrow(() -> new SafeFoodException(SafeFoodException.ERROR_USER_NOT_FOUND));

        String digitCode;
        do {
            digitCode = AppUtils.generateDigitCode();
        } while (userRepository.existsByResetToken(digitCode));
        user.setExpiredTime(user.getExpiredTime().plusMillis(TIME_PLUS));
        user.setResetToken(digitCode);
        userRepository.save(user);

        sendDigitCodeToMail(user.getEmail(), digitCode, Constants.RESET_YOUR_PASSWORD, Constants.TEMPLATE_FORGOT_PASSWORD_FORM);

    }

    public String verifyForgotPasswordDigitCode(CodeRequestDto codeRequestDto) {
        User user = userRepository.findByResetToken(codeRequestDto.getCode()).orElseThrow(
                () -> new SafeFoodException(SafeFoodException.ERROR_USER_NOT_FOUND)
        );
        if (Instant.now().isBefore(user.getExpiredTime())) {
            return tokenProvider.createAuthTokenForgotPassword(user.getEmail());
        } else if (Instant.now().isAfter(user.getExpiredTime())) {
            user.setResetToken(null);
            user.setExpiredTime(null);
            userRepository.save(user);
        }
        throw new SafeFoodException(SafeFoodException.DIGIT_CODE_HAS_EXPIRED);
    }

    public void resetPassword(ResetPasswordRequestDto resetPasswordRequestDto) {
        String email = tokenProvider.getUsername(resetPasswordRequestDto.getAuthToken()).toString();
        User user = userRepository.findByEmailAndProvider(email, AuthProvider.local).orElseThrow(
                () -> new SafeFoodException(SafeFoodException.ERROR_USER_NOT_FOUND)
        );
        if (checkPasswordInValid(resetPasswordRequestDto.getNewPassword())) {
            throw new SafeFoodException(SafeFoodException.PASSWORD_INPUT_IS_INVALID);
        }
        user.setPassword(passwordEncoder.encode(resetPasswordRequestDto.getNewPassword()));
        if (user.getRole().getName().equals(Constants.ROLE_USER)) {
            user.setStatus(UserEnum.Status.ACTIVED);
        }
        user.setExpiredTime(null);
        user.setResetToken(null);
        userRepository.save(user);
    }

    public UserAuthenticationResponseDto signInWithGoogleApp(OAuth2RequestDto dto) {
        // Get user information from Google via access token
        HttpHeaders headers1 = new HttpHeaders();
        headers1.add(HttpHeaders.AUTHORIZATION, dto.getTokenType() + " " + dto.getAccessToken());
        HttpEntity entity1 = new HttpEntity(headers1);
        GoogleAccountDto googleAccountDTO = restTemplate.exchange(Constants.GOOGLE_INFO_URI, HttpMethod.GET, entity1, GoogleAccountDto.class).getBody();
        Optional<User> oUser = userRepository.findFirstByOauthIdAndProvider(googleAccountDTO.getId(), AuthProvider.google);
        User user = oUser.orElseGet(() -> createUserByGoogle(googleAccountDTO));
        UserAuthenticationResponseDto userAuthenticationDTO = new UserAuthenticationResponseDto();
        Authentication authentication = new UsernamePasswordAuthenticationToken(SafeFoodUserPrincipal.create(user, JwtUserdetailService.getAuthorities(user)), null, null);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String accessToken = tokenProvider.generateAccessToken(authentication, false);
        userAuthenticationDTO.setAccessToken(accessToken);
        userAuthenticationDTO.setTokenType("Bearer");
        userAuthenticationDTO.setExpired(tokenProvider.generateExpiredTime());
        userAuthenticationDTO.setRefreshToken(tokenProvider.generateAccessToken(authentication, false));
        userAuthenticationDTO.setUserId(user.getId());
        userAuthenticationDTO.setStatus(user.getStatus());

        if (null != dto.getDeviceToken() && !dto.getDeviceToken().isEmpty()) {
            handleUserSaveDeviceToken(user, dto.getDeviceToken());
        }
        return userAuthenticationDTO;
    }

    private User createUserByGoogle(GoogleAccountDto googleAccountDTO) {
        User user = new User();
        user.setStatus(UserEnum.Status.ACTIVED);
        user.setProvider(AuthProvider.google);
        user.setEmail(googleAccountDTO.getEmail());
        user.setOauthId(googleAccountDTO.getId());
        user.setRole(this.roleRepository.findFirstByName(Constants.ROLE_USER).orElse(null));
        userRepository.save(user);

        UserInformation userInformation = new UserInformation();
        userInformation.setUser(user);
        userInformation.setFirstName(googleAccountDTO.getFamily_name());
        userInformation.setLastName(googleAccountDTO.getGiven_name());
        userInformation.setUserImage(googleAccountDTO.getPicture());
        userInformationRepository.save(userInformation);
        return user;
    }

    public UserAuthenticationResponseDto signInWithFacebookApp(OAuth2RequestDto dto) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, "application/json;charset=utf-8");
        HttpEntity<String> entity = new HttpEntity<>(headers);
        String facebookGetInfoUrl = Constants.FACEBOOK_INFO_URI + "&access_token=" + dto.getAccessToken();
        FacebookAccountDto facebookAccountDTO = restTemplate.exchange(facebookGetInfoUrl, HttpMethod.GET, entity, FacebookAccountDto.class).getBody();
        Optional<User> oUser = this.userRepository.findFirstByOauthIdAndProvider(facebookAccountDTO.getId(), AuthProvider.facebook);
        User user = oUser.orElseGet(() -> createUserByFacebook(facebookAccountDTO));
        UserAuthenticationResponseDto userAuthenticationDTO = new UserAuthenticationResponseDto();
        Authentication authentication = new UsernamePasswordAuthenticationToken(SafeFoodUserPrincipal.create(user, JwtUserdetailService.getAuthorities(user)), null, null);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String accessToken = tokenProvider.generateAccessToken(authentication, false);
        userAuthenticationDTO.setAccessToken(accessToken);
        userAuthenticationDTO.setTokenType("Bearer");
        userAuthenticationDTO.setExpired(tokenProvider.generateExpiredTime());
        userAuthenticationDTO.setRefreshToken(tokenProvider.generateAccessToken(authentication, false));
        userAuthenticationDTO.setUserId(user.getId());
        userAuthenticationDTO.setStatus(user.getStatus());

        if (null != dto.getDeviceToken() && !dto.getDeviceToken().isEmpty()) {
            handleUserSaveDeviceToken(user, dto.getDeviceToken());
        }
        return userAuthenticationDTO;
    }

    private User createUserByFacebook(FacebookAccountDto facebookAccountDTO) {
        User user = new User();
        user.setStatus(UserEnum.Status.ACTIVED);
        user.setProvider(AuthProvider.facebook);
        if (null != facebookAccountDTO.getEmail()) {
            user.setEmail(facebookAccountDTO.getEmail());
        }
        user.setOauthId(facebookAccountDTO.getId());
        user.setRole(this.roleRepository.findFirstByName(Constants.ROLE_USER).orElse(null));
        userRepository.save(user);

        UserInformation userInformation = new UserInformation();
        userInformation.setUser(user);
        userInformation.setFirstName(facebookAccountDTO.getFirstName());
        userInformation.setLastName(facebookAccountDTO.getLastName());
        Map<String, Object> pictureObj = facebookAccountDTO.getPicture();
        if (pictureObj.containsKey("data")) {
            Map<String, Object> dataObj = (Map<String, Object>) pictureObj.get("data");
            if (dataObj.containsKey("url")) {
                userInformation.setUserImage((String) dataObj.get("url"));
            }
        }
        userInformationRepository.save(userInformation);
        return user;
    }

    @Transactional(readOnly = true)
    public UserProfileDto getProfileUser() {
        User user = appUtils.getCurrentUser().orElseThrow(() -> new SafeFoodException(SafeFoodException.ERROR_USER_NOT_FOUND));
        return userInformationRepository.getProfileUser(user.getId());
    }

    @Transactional(rollbackFor = SafeFoodException.class)
    public UserProfileDto updateProfileUser(UserProfileDto userProfileDto) {
        User user = getCurrentUser().get();
        user.getUserInformation().setUserImage(userProfileDto.getUserImage());
        user.getUserInformation().setFirstName(userProfileDto.getFirstName());
        user.getUserInformation().setLastName(userProfileDto.getLastName());
        user.getUserInformation().setGender(userProfileDto.getGender());
        user.getUserInformation().setBirthday(userProfileDto.getBirthday());
        userRepository.save(user);
        return userProfileDto;
    }

    public String userUpdateDeviceToken(DeviceTokenDTO deviceTokenDTO) {
        User user = getCurrentUser().get();
        user.setDeviceToken(deviceTokenDTO.getDeviceToken());
        user.setIsLogin(true);
        user = userRepository.save(user);
        return user.getDeviceToken();
    }

    public void userLogout() {
        User user = getCurrentUser().get();
        user.setDeviceToken(null);
        user.setIsLogin(false);
        userRepository.save(user);
    }

    private void handleUserSaveDeviceToken(User user, String deviceToken) {
        user.setDeviceToken(deviceToken);
        user.setIsLogin(true);
        userRepository.save(user);
    }

}
