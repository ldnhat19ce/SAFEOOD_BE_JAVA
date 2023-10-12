package tech.dut.safefood.controller.user;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import tech.dut.safefood.dto.request.UpdateSeenFlagRequestDto;
import tech.dut.safefood.dto.response.APIResponse;
import tech.dut.safefood.dto.response.NotificationResponseDto;
import tech.dut.safefood.enums.CommonEnum;
import tech.dut.safefood.service.User.UserNotificationService;
import tech.dut.safefood.vo.PageInfo;

import java.util.List;

@RestController
@RequestMapping("/user/notification")
@RequiredArgsConstructor
public class UserNotificationController {
    private static final String GET_ALL_NOTIFICATION = "";
    private static final String UPDATE_SEEN_FLAG_NOTIFICATION = "/seen-flag";

    private final UserNotificationService userNotificationService;

    @ApiOperation("Get all notification by current user login")
    @GetMapping(GET_ALL_NOTIFICATION)
    public APIResponse<PageInfo<NotificationResponseDto>> getVoucherQuery(
            @ApiParam(value = "Page")
            @RequestParam(required = false) Integer page,
            @ApiParam(value = "Limit")
            @RequestParam(required = false) Integer limit,
            @ApiParam(value = "Types")
            @RequestParam(required = false) List<CommonEnum.NotificationType> types) {
        return APIResponse.okStatus(userNotificationService.getAllNotification(page, limit, types));
    }

    @ApiOperation("Update seen flag")
    @PutMapping(UPDATE_SEEN_FLAG_NOTIFICATION)
    public APIResponse<?> updateSeenFlag(@RequestBody UpdateSeenFlagRequestDto requestDto) {
        userNotificationService.updateSeenFlagNotification(requestDto);
        return APIResponse.okStatus();
    }
}
