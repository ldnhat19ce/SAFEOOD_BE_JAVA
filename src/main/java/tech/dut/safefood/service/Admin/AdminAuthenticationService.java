package tech.dut.safefood.service.Admin;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.dut.safefood.component.JwtTokenProvider;
import tech.dut.safefood.dto.request.ChangePasswordRequestDto;
import tech.dut.safefood.dto.response.AdminAuthenticationResponseDto;
import tech.dut.safefood.exception.SafeFoodException;
import tech.dut.safefood.model.Role;
import tech.dut.safefood.model.SafeFoodUserPrincipal;
import tech.dut.safefood.model.User;
import tech.dut.safefood.repository.UserRepository;
import tech.dut.safefood.service.JwtUserdetailService;
import tech.dut.safefood.util.constants.Constants;

import javax.validation.constraints.NotBlank;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AdminAuthenticationService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional(rollbackFor = Exception.class)
    public AdminAuthenticationResponseDto login(@NotBlank String email, @NotBlank String password) throws SafeFoodException {
        User user = userRepository.findByEmail(email).orElseThrow(() ->
                new SafeFoodException(SafeFoodException.ERROR_USER_NOT_FOUND));
        if (!user.getRole().getName().equals(Constants.ROLE_ADMIN)) {
            throw new SafeFoodException(SafeFoodException.ERROR_USER_NOT_FOUND);
        }
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new SafeFoodException(SafeFoodException.UNAUTHORIZED_INVALID_PASSWORD);
        }
        Authentication authentication = new UsernamePasswordAuthenticationToken(SafeFoodUserPrincipal.create(user, JwtUserdetailService.getAuthorities(user)), null, null);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String accessToken = tokenProvider.generateAccessToken(authentication, true);
        AdminAuthenticationResponseDto adminAuthenticationResponseDto = new AdminAuthenticationResponseDto();
        adminAuthenticationResponseDto.setToken(accessToken);
        adminAuthenticationResponseDto.setTokenType("Bearer");
        adminAuthenticationResponseDto.setExpired(tokenProvider.generateExpiredTime());
        adminAuthenticationResponseDto.setRefreshToken(tokenProvider.generateAccessToken(authentication, false));
        adminAuthenticationResponseDto.setUserId(user.getId());
        return adminAuthenticationResponseDto;
    }

    private boolean checkPasswordInValid(String password) {
        return !password.matches(Constants.PASSWORD_PATTERN);
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

}
