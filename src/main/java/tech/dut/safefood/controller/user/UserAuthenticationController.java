package tech.dut.safefood.controller.user;

import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import tech.dut.safefood.dto.SignUpUserDto;
import tech.dut.safefood.dto.request.*;
import tech.dut.safefood.dto.response.APIResponse;
import tech.dut.safefood.dto.response.UserAuthenticationResponseDto;
import tech.dut.safefood.service.User.UserAuthenticationService;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

@RestController
@RequestMapping("/user/authentication")
@Validated
public class UserAuthenticationController {
    private static final String USER_UPDATE_DEVICE_TOKEN_ENDPOINT = "/device-token";
    private static final String USER_LOGOUT_ENDPOINT = "/logout";

    @Autowired
    private UserAuthenticationService userAuthenticationService;

    @ApiOperation("API for user sign in by email and password")
    @PostMapping("/login")
    public APIResponse<UserAuthenticationResponseDto> userSignIn(@Valid @RequestBody LoginRequestDto signInDTO) {
        UserAuthenticationResponseDto userAuthenticationResponseDto = userAuthenticationService.loginNormal(signInDTO);
        return APIResponse.okStatus(userAuthenticationResponseDto);
    }

    @ApiOperation("User active accont by code")
    @PostMapping("/sign-up/active")
    public APIResponse<UserAuthenticationResponseDto> activeAccount(@RequestBody CodeRequestDto codeRequestDto) {
        return APIResponse.okStatus(userAuthenticationService.activateAccount(codeRequestDto));
    }

    @ApiOperation("User re-send digit code")
    @PostMapping("/sign-up/re-send-code")
    public APIResponse<?> resendDigitCode(@Valid @RequestBody ResendDigitCodeRequestDto resendDigitCodeDTO) {
        userAuthenticationService.resendDigitCode(resendDigitCodeDTO);
        return APIResponse.okStatus();
    }

    @ApiOperation("API for sign up normal user")
    @PostMapping("/sign-up")
    public APIResponse<SignUpUserDto> signUpUser(@Valid @RequestBody SignUpUserDto signUpUserDto) {
        return APIResponse.okStatus(userAuthenticationService.signUpUser(signUpUserDto));
    }

    @ApiOperation("User change password")
    @PostMapping("/change-password")
    public APIResponse<?> changePassword(@Valid @RequestBody ChangePasswordRequestDto changePasswordRequestDto) {

        userAuthenticationService.changePassword(changePasswordRequestDto);
        return APIResponse.okStatus();
    }

    @ApiOperation("User Update Avatart")
    @PatchMapping("/update-avatar")
    public APIResponse<?> updateAvatar(@Valid @RequestBody UserAvatarDto avatar) {

        userAuthenticationService.updateAvatar(avatar);
        return APIResponse.okStatus();
    }

    @ApiOperation("User forgot password")
    @PostMapping("/forgot-password")
    public APIResponse<?> userForgotPassword(@Valid @RequestBody ForgotPasswordRequestDto forgotPasswordDTO) {
        userAuthenticationService.forgotPassword(forgotPasswordDTO);
        return APIResponse.okStatus();
    }

    @ApiOperation("User send forgot password digit code to verify")
    @PostMapping("/forgot-password/verify-code")
    public APIResponse<String> verifyForgotPasswordDigitCode(@Valid @RequestBody CodeRequestDto codeRequestDto) {
        return APIResponse.okStatus(userAuthenticationService.verifyForgotPasswordDigitCode(codeRequestDto));
    }

    @ApiOperation("User reset password")
    @PostMapping("/forgot-password/reset")
    public APIResponse<?> resetPassword(@Valid @RequestBody ResetPasswordRequestDto resetPasswordDTO) {
        userAuthenticationService.resetPassword(resetPasswordDTO);
        return APIResponse.okStatus();
    }

    @ApiOperation("User sign in by Google app")
    @PostMapping("/login-app/google")
    public APIResponse<UserAuthenticationResponseDto> userSignInByGoogleApp(@RequestBody OAuth2RequestDto dto) {
        return APIResponse.okStatus(userAuthenticationService.signInWithGoogleApp(dto));
    }

    @ApiOperation("User sign in by Facebook app")
    @PostMapping("/login-app/facebook")
    public APIResponse<UserAuthenticationResponseDto> userSignInByFaceBookApp(@RequestBody OAuth2RequestDto dto) {
        return APIResponse.okStatus(userAuthenticationService.signInWithFacebookApp(dto));
    }

    @ApiOperation("API for update device token")
    @PutMapping(USER_UPDATE_DEVICE_TOKEN_ENDPOINT)
    public APIResponse<String> userUpdateDeviceToken(@RequestBody DeviceTokenDTO deviceTokenDTO) {
        return APIResponse.okStatus(userAuthenticationService.userUpdateDeviceToken(deviceTokenDTO));
    }

    @ApiOperation("API for logout")
    @PutMapping(USER_LOGOUT_ENDPOINT)
    public APIResponse<?> userLogout() {
        userAuthenticationService.userLogout();
        return APIResponse.okStatus();
    }
}
