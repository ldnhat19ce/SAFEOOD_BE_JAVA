package tech.dut.safefood.service.Shop;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import tech.dut.safefood.component.JwtTokenProvider;
import tech.dut.safefood.dto.request.ChangePasswordRequestDto;
import tech.dut.safefood.dto.request.LoginRequestDto;
import tech.dut.safefood.dto.SignUpShopDto;
import tech.dut.safefood.dto.request.ShopProfileRequestDto;
import tech.dut.safefood.dto.response.ShopAuthenticationDto;
import tech.dut.safefood.dto.response.ShopProfileResponseDto;
import tech.dut.safefood.dto.response.UserAuthenticationResponseDto;
import tech.dut.safefood.enums.AuthProvider;
import tech.dut.safefood.enums.UserEnum;
import tech.dut.safefood.exception.SafeFoodException;
import tech.dut.safefood.model.*;
import tech.dut.safefood.repository.RoleRepository;
import tech.dut.safefood.repository.ShopImageRepository;
import tech.dut.safefood.repository.ShopRepository;
import tech.dut.safefood.repository.UserRepository;
import tech.dut.safefood.service.JwtUserdetailService;
import tech.dut.safefood.util.AppUtils;
import tech.dut.safefood.util.constants.Constants;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ShopAuthenticationService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private ShopRepository shopRepository;

    @Value("${email.safefood.time.plusMillis}")
    private Long TIME_PLUS;

    @Autowired
    private AppUtils appUtils;

    @Autowired
    private ShopImageRepository shopImageRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional(rollbackFor = Exception.class)
    public ShopAuthenticationDto shopLogin(LoginRequestDto loginRequestDto) throws SafeFoodException {
        User user = userRepository.findByEmail(loginRequestDto.getEmail()).orElseThrow(() -> new SafeFoodException(SafeFoodException.EMAIL_OR_PASSWORD_IS_INVALID));
        if (!encoder.matches(loginRequestDto.getPassword(), user.getPassword())) {
            throw new SafeFoodException(SafeFoodException.EMAIL_OR_PASSWORD_IS_INVALID);
        }
        Role role = roleRepository.findFirstByName(Constants.ROLE_SHOP).orElse(null);
        if (!user.getRole().getName().equals(Constants.ROLE_SHOP)) {
            throw new SafeFoodException(SafeFoodException.ERROR_USER_NOT_FOUND);
        }
        ShopAuthenticationDto shopAuthenticationDto = new ShopAuthenticationDto();
        UserAuthenticationResponseDto shopAuthenticationDtouserAuthenticationDTO = new UserAuthenticationResponseDto();
        if (UserEnum.Status.INACTIVE.equals(user.getStatus())) {
            throw new SafeFoodException(SafeFoodException.ERROR_SHOP_NOT_ACTIVE);
        }
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(SafeFoodUserPrincipal.create(user, JwtUserdetailService.getAuthorities(user)), user.getPassword(), JwtUserdetailService.getAuthorities(user));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String accessToken = tokenProvider.generateAccessToken(authentication, false);
        shopAuthenticationDto.setAccessToken(accessToken);
        shopAuthenticationDto.setTokenType("Bearer");
        shopAuthenticationDto.setExpired(tokenProvider.generateExpiredTime());
        shopAuthenticationDto.setRefreshToken(tokenProvider.generateAccessToken(authentication, false));
        shopAuthenticationDto.setUserId(user.getId());
        shopAuthenticationDto.setStatus(user.getStatus());
        return shopAuthenticationDto;
    }

    @Transactional(rollbackFor = SafeFoodException.class)
    public SignUpShopDto signUpShop(SignUpShopDto shopRequestDto) {
        if (userRepository.existsByEmailIgnoreCaseAndProvider(shopRequestDto.getEmail(), AuthProvider.local)) {
            throw new SafeFoodException(SafeFoodException.ERROR_EMAIL_ALREADY_EXISTS);
        }
        if (null != shopRequestDto.getPhoneNumber() && shopRepository.existsByPhoneIgnoreCase(shopRequestDto.getPhoneNumber())) {
            throw new SafeFoodException(SafeFoodException.ERROR_PHONE_NUMBER_ALREADY_EXISTS);
        }
        if (this.checkPasswordInValid(shopRequestDto.getPassword())) {
            throw new SafeFoodException(SafeFoodException.PASSWORD_INPUT_IS_INVALID);
        }
        User user = new User();
        user.setPassword(encoder.encode(shopRequestDto.getPassword()));
        user.setEmail(shopRequestDto.getEmail().toLowerCase());
        user.setStatus(UserEnum.Status.INACTIVE);
        user.setProvider(AuthProvider.local);
        user.setRole(roleRepository.findFirstByName(Constants.ROLE_SHOP).orElse(null));
        user.setExpiredTime(Instant.now().plusMillis(TIME_PLUS));

        Address address = new Address();
        address.setCity(shopRequestDto.getCity());
        address.setDistrict(shopRequestDto.getDistrict());
        address.setTown(shopRequestDto.getTown());
        address.setStreet(shopRequestDto.getStreet());
        address.setX(shopRequestDto.getX());
        address.setY(shopRequestDto.getY());

        Shop shop = new Shop();
        shop.setName(shopRequestDto.getName());
        shop.setBanner(shopRequestDto.getBanner());
        shop.setDescription(shopRequestDto.getDescription());
        shop.setUser(user);
        address.setShop(shop);
        shop.setAddresses(address);
        if (null != shopRequestDto.getPhoneNumber()) {
            user.setPhoneNumber(shopRequestDto.getPhoneNumber());
            shop.setPhone(shopRequestDto.getPhoneNumber());
        }
        shopRepository.save(shop);
        return shopRequestDto;
    }


    @Transactional(rollbackFor = SafeFoodException.class, isolation = Isolation.SERIALIZABLE)
    public void updateBannerShop(List<String> images) {
        Shop shop = appUtils.getShopCurrent().orElseThrow(() -> new SafeFoodException(SafeFoodException.ERROR_USER_NOT_IS_SHOP));
        List<ShopImage> shopImages = new ArrayList<>();
        for (String image : images) {
            ShopImage shopImage = new ShopImage();
            shopImage.setImage(image);
            shopImage.setShop(shop);
            shopImages.add(shopImage);
        }
        shopImageRepository.saveAll(shopImages);
    }

    @Transactional(readOnly = true)
    public ShopProfileResponseDto getProfile() {
        Shop shop = appUtils.getShopCurrent().orElseThrow(() -> new SafeFoodException(SafeFoodException.ERROR_USER_NOT_IS_SHOP));
        ShopProfileResponseDto shopProfileResponseDto = new ShopProfileResponseDto();
        shopProfileResponseDto.setId(shop.getId());
        shopProfileResponseDto.setName(shop.getName());
        shopProfileResponseDto.setLogo(shop.getBanner());
        shopProfileResponseDto.setDescription(shop.getDescription());
        shopProfileResponseDto.setPhone(shop.getPhone());
        List<String> imagesShop = shop.getShopImages().stream().map(ShopImage::getImage).collect(Collectors.toList());
        shopProfileResponseDto.setImageShops(imagesShop);
        shopProfileResponseDto.setRantings(shop.getRatings());
        shopProfileResponseDto.setCity(shop.getAddresses().getCity());
        shopProfileResponseDto.setDistrict(shop.getAddresses().getDistrict());
        shopProfileResponseDto.setTown(shop.getAddresses().getTown());
        shopProfileResponseDto.setStreet(shop.getAddresses().getStreet());
        shopProfileResponseDto.setX(shop.getAddresses().getX());
        shopProfileResponseDto.setY(shop.getAddresses().getY());
        return shopProfileResponseDto;
    }

    @Transactional(rollbackFor = SafeFoodException.class)
    public void UpdateProfile(ShopProfileRequestDto shopProfileRequestDto) {
        Shop shop = appUtils.getShopCurrent().orElseThrow(() -> new SafeFoodException(SafeFoodException.ERROR_USER_NOT_IS_SHOP));
        Long shopId = shop.getId();
        Shop tmp = shopRepository.findById(shopId).get();
        if (shopProfileRequestDto.getStatus().equals(Constants.SHOP_SCHEDULE_CLOSE)  && shopProfileRequestDto.getStatus().equals(Constants.SHOP_SCHEDULE_OPEN)) {
            throw new SafeFoodException(SafeFoodException.ERROR_SHOP_SCHEDULE_NOT_CORRECT);
        }
        tmp.setScheduleActive(shopProfileRequestDto.getStatus());
        tmp.setDescription(shopProfileRequestDto.getDescription());
        tmp.setBanner(shopProfileRequestDto.getLogo());
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
