package tech.dut.safefood.controller.admin;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import tech.dut.safefood.dto.response.APIResponse;
import tech.dut.safefood.dto.response.AdminReviewDetailResponseDto;
import tech.dut.safefood.dto.response.ReviewResponseDto;
import tech.dut.safefood.service.Admin.AdminCommunityService;
import tech.dut.safefood.vo.PageInfo;

@RestController
@RequestMapping(path = "/admin/community")
@RequiredArgsConstructor
public class AdminCommunityController {
    private static final String ADMIN_GET_ALL_REVIEW_ENDPOINT = "/reviews";
    private static final String ADMIN_DELETE_REVIEW_ENDPOINT = "/review/{id}";
    private static final String ADMIN_DELETE_REPLY_ENDPOINT = "/review/reply/{id}";
    private static final String ADMIN_GET_DETAIL_REVIEW_ENDPOINT = "/review/{id}";

    private final AdminCommunityService adminCommunityService;

    @ApiOperation("Admin delete review")
    @DeleteMapping(ADMIN_DELETE_REVIEW_ENDPOINT)
    public APIResponse<?> adminDeleteReview(@PathVariable Integer id) {
        adminCommunityService.adminDeleteReview(id);
        return APIResponse.okStatus();
    }

    @ApiOperation("Admin delete reply")
    @DeleteMapping(ADMIN_DELETE_REPLY_ENDPOINT)
    public APIResponse<?> adminDeleteReply(@PathVariable Integer id) {
        adminCommunityService.adminDeleteReply(id);
        return APIResponse.okStatus();
    }

    @ApiOperation("Admin get review detail")
    @GetMapping(ADMIN_GET_DETAIL_REVIEW_ENDPOINT)
    public APIResponse<AdminReviewDetailResponseDto> adminGetReviewDetail(@PathVariable Long id) {
        return APIResponse.okStatus(adminCommunityService.adminGetReviewDetail(id));
    }

    @ApiOperation("Admin get all review")
    @GetMapping(ADMIN_GET_ALL_REVIEW_ENDPOINT)
    public APIResponse<PageInfo<ReviewResponseDto>> adminGetAllReview(
            @ApiParam(value = "Limit")
            @RequestParam(required = false) Integer limit,
            @ApiParam(value = "Page")
            @RequestParam(required = false) Integer page,
            @ApiParam(value = "Shop id")
            @RequestParam(required = false) Long shopId) {
        return APIResponse.okStatus(adminCommunityService.adminGetAllReview(limit, page, shopId));
    }
}
