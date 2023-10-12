package tech.dut.safefood.controller.shop;

import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import tech.dut.safefood.dto.SignUpShopDto;
import tech.dut.safefood.dto.SignUpUserDto;
import tech.dut.safefood.dto.request.ChangePasswordRequestDto;
import tech.dut.safefood.dto.request.LoginRequestDto;
import tech.dut.safefood.dto.request.ShopProfileRequestDto;
import tech.dut.safefood.dto.response.APIResponse;
import tech.dut.safefood.dto.response.ShopAuthenticationDto;
import tech.dut.safefood.dto.response.ShopProfileResponseDto;
import tech.dut.safefood.service.Shop.ShopAuthenticationService;
import tech.dut.safefood.service.User.UserAuthenticationService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/shop/authentication")
@Validated
public class ShopAuthenticationController {

    @Autowired
    private UserAuthenticationService userAuthenticationService;

    private static final String USER_LOGOUT_ENDPOINT = "/logout";


    @Autowired
    private ShopAuthenticationService shopAuthenticationService;

    @ApiOperation("API for logout")
    @PutMapping(USER_LOGOUT_ENDPOINT)
    public APIResponse<?> userLogout() {
        userAuthenticationService.userLogout();
        return APIResponse.okStatus();
    }

    @ApiOperation("API for Shop sign in by email and password")
    @PostMapping("/login")
    public APIResponse<ShopAuthenticationDto> shopSignIn(@Valid @RequestBody LoginRequestDto signInDTO) {
        ShopAuthenticationDto shopAuthenticationDto = shopAuthenticationService.shopLogin(signInDTO);
        return APIResponse.okStatus(shopAuthenticationDto);
    }

    @ApiOperation("API for sign up shop")
    @PostMapping("/sign-up")
    public APIResponse<SignUpShopDto> signUpUser(@Valid @RequestBody SignUpShopDto signUpShopDto) {
        return APIResponse.okStatus(shopAuthenticationService.signUpShop(signUpShopDto));
    }

    @ApiOperation("API for update banner")
    @PostMapping("/banner")
    public APIResponse<?> signUpUser(@Valid @RequestBody List<String> images) {
        shopAuthenticationService.updateBannerShop(images);
        return APIResponse.okStatus();
    }

    @ApiOperation("API for get profile shop")
    @GetMapping("/profile")
    public APIResponse<ShopProfileResponseDto> getProfile() {
        return APIResponse.okStatus(shopAuthenticationService.getProfile());
    }

    @ApiOperation("API for get profile shop")
    @PutMapping("/update-profile")
    public APIResponse<?> getProfile(@RequestBody ShopProfileRequestDto shopProfileRequestDto) {
        shopAuthenticationService.UpdateProfile(shopProfileRequestDto);
        return APIResponse.okStatus();
    }

    @ApiOperation("Admin change password")
    @PostMapping("/change-password")
    public APIResponse<?> changePassword(@Valid @RequestBody ChangePasswordRequestDto changePasswordRequestDto) {

        shopAuthenticationService.changePassword(changePasswordRequestDto);
        return APIResponse.okStatus();
    }

}
