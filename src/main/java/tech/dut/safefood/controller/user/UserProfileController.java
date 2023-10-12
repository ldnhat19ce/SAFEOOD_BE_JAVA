package tech.dut.safefood.controller.user;

import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tech.dut.safefood.dto.response.APIResponse;
import tech.dut.safefood.dto.UserProfileDto;
import tech.dut.safefood.service.User.UserAuthenticationService;

@RestController
@RequestMapping("/user/profile")
public class UserProfileController {
    @Autowired
    private UserAuthenticationService userAuthenticationService;

    @ApiOperation("API for user get profile")
    @GetMapping
    public APIResponse<UserProfileDto> getProfile() {
        return APIResponse.okStatus(userAuthenticationService.getProfileUser());
    }

    @ApiOperation("API for user get profile")
    @PostMapping
    public APIResponse<UserProfileDto> updateProfile(@RequestBody UserProfileDto userProfileDto) {
        return APIResponse.okStatus(userAuthenticationService.updateProfileUser(userProfileDto));
    }
}
