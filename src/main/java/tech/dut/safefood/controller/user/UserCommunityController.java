package tech.dut.safefood.controller.user;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import tech.dut.safefood.dto.request.ReplyRequestDto;
import tech.dut.safefood.dto.request.ReviewRequestDto;
import tech.dut.safefood.dto.response.*;
import tech.dut.safefood.service.User.UserCommunityService;
import tech.dut.safefood.vo.PageInfo;

import javax.validation.Valid;

@RestController
@RequestMapping("/user/community")
@RequiredArgsConstructor
@Validated
public class UserCommunityController {
    private static final String USER_SAVE_REVIEW_ENDPOINT = "/reviews";
    private static final String USER_DELETE_REVIEW_ENDPOINT = "/review/{id}";
    private static final String USER_EDIT_REVIEW_ENDPOINT = "/review/{id}";
    private static final String USER_LIKE_OR_DISLIKE_REVIEW_ENDPOINT = "/review/{id}/favorite";
    private static final String USER_SAVE_REPLY_ENDPOINT = "/review/{id}/replies";
    private static final String USER_LIKE_OR_DISLIKE_REPLY_ENDPOINT = "/review/reply/{id}/favorite";
    private static final String USER_DELETE_REPLY_ENDPOINT = "/review/reply/{id}";
    private static final String USER_EDIT_REPLY_ENDPOINT = "/review/reply/{id}";
    private static final String USER_GET_ALL_REVIEW_ENDPOINT = "/reviews";
    private static final String USER_GET_DETAIL_REVIEW_ENDPOINT = "/review/{id}";
    private static final String USER_GET_TOP_REVIEW_ENDPOINT = "/review/top";

    private final UserCommunityService userCommunityService;

    @ApiOperation("Create new review")
    @PostMapping(USER_SAVE_REVIEW_ENDPOINT)
    public APIResponse<ReviewCreateResponseDto> createReview(@RequestBody @Valid ReviewRequestDto reviewRequestDTO) {
        return APIResponse.okStatus(userCommunityService.createReview(reviewRequestDTO));
    }

    @ApiOperation("Create new reply")
    @PostMapping(USER_SAVE_REPLY_ENDPOINT)
    public APIResponse<ReplyCreateResponseDto> userSaveReply(@PathVariable Integer id, @RequestBody ReplyRequestDto replyRequestDto) {
        return APIResponse.okStatus(userCommunityService.createReply(replyRequestDto, id));
    }

    @ApiOperation("User like or dislike review")
    @PostMapping(USER_LIKE_OR_DISLIKE_REVIEW_ENDPOINT)
    public APIResponse<?> userLikeOrDislikeReview(@PathVariable Integer id) {
        userCommunityService.userLikeOrDislikeReview(id);
        return APIResponse.okStatus();
    }

    @ApiOperation("User like or dislike reply")
    @PostMapping(USER_LIKE_OR_DISLIKE_REPLY_ENDPOINT)
    public APIResponse<?> userLikeOrDislikeReply(@PathVariable Integer id) {
        userCommunityService.userLikeOrDislikeReply(id);
        return APIResponse.okStatus();
    }

    @ApiOperation("User delete review")
    @DeleteMapping(USER_DELETE_REVIEW_ENDPOINT)
    public APIResponse<?> userDeleteReview(@PathVariable Integer id) {
        userCommunityService.userDeleteReview(id);
        return APIResponse.okStatus();
    }

    @ApiOperation("User delete reply")
    @DeleteMapping(USER_DELETE_REPLY_ENDPOINT)
    public APIResponse<?> userDeleteReply(@PathVariable Integer id) {
        userCommunityService.userDeleteReply(id);
        return APIResponse.okStatus();
    }

    @ApiOperation("Edit review")
    @PutMapping(USER_EDIT_REVIEW_ENDPOINT)
    public APIResponse<ReviewCreateResponseDto> editReview(
            @PathVariable Long id,
            @RequestBody @Valid ReviewRequestDto reviewRequestDTO) {
        return APIResponse.okStatus(userCommunityService.editReview(reviewRequestDTO, id));
    }

    @ApiOperation("Edit reply")
    @PutMapping(USER_EDIT_REPLY_ENDPOINT)
    public APIResponse<ReplyCreateResponseDto> editReply(@PathVariable Long id, @RequestBody String content) {
        return APIResponse.okStatus(userCommunityService.editReply(content, id));
    }

    @ApiOperation("User get review detail")
    @GetMapping(USER_GET_DETAIL_REVIEW_ENDPOINT)
    public APIResponse<ReviewDetailResponseDto> userGetReviewDetail(@PathVariable Long id) {
        return APIResponse.okStatus(userCommunityService.userGetReviewDetail(id));
    }

    @ApiOperation("User get top review")
    @GetMapping(USER_GET_TOP_REVIEW_ENDPOINT)
    public APIResponse<PageInfo<TopReviewResponseDto>> userGetTopReview(
            @ApiParam(value = "Limit")
            @RequestParam(required = false) Integer limit,
            @ApiParam(value = "Page")
            @RequestParam(required = false) Integer page,
            @ApiParam(value = "shopId")
            @RequestParam(required = false) Long shopId) {
        return APIResponse.okStatus(userCommunityService.userGetTopReview(shopId, limit, page));
    }

    @ApiOperation("User get all review")
    @GetMapping(USER_GET_ALL_REVIEW_ENDPOINT)
    public APIResponse<PageInfo<ReviewResponseDto>> userGetAllReview(
            @ApiParam(value = "Limit")
            @RequestParam(required = false) Integer limit,
            @ApiParam(value = "Page")
            @RequestParam(required = false) Integer page,
            @ApiParam(value = "shopId")
            @RequestParam(required = false) Long shopId,
            @ApiParam(value = "Filter", allowableValues = "MOST_LIKED", defaultValue = "MOST_LIKED")
            @RequestParam String filter) {
        return APIResponse.okStatus(userCommunityService.userGetAllReview(shopId, limit, page, filter));
    }
}
