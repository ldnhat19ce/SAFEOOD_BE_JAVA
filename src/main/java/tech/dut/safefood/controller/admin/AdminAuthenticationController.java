package tech.dut.safefood.controller.admin;


import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.dut.safefood.dto.request.ChangePasswordRequestDto;
import tech.dut.safefood.dto.request.LoginRequestDto;
import tech.dut.safefood.dto.response.APIResponse;
import tech.dut.safefood.dto.response.AdminAuthenticationResponseDto;
import tech.dut.safefood.service.Admin.AdminAuthenticationService;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/admin/authentication")
@Validated
public class AdminAuthenticationController {

    @Autowired
    private AdminAuthenticationService adminAuthenticationService;

    @ApiOperation("Admin sign in")
    @PostMapping(value = "/login")
    public APIResponse<AdminAuthenticationResponseDto> login(@Valid @RequestBody LoginRequestDto request) {
        AdminAuthenticationResponseDto responseData = adminAuthenticationService.login(request.getEmail(), request.getPassword());
        return APIResponse.okStatus(responseData);
    }

    @ApiOperation("Admin change password")
    @PostMapping("/change-password")
    public APIResponse<?> changePassword(@Valid @RequestBody ChangePasswordRequestDto changePasswordRequestDto) {

        adminAuthenticationService.changePassword(changePasswordRequestDto);
        return APIResponse.okStatus();
    }

}
