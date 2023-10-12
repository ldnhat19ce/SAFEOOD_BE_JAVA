package tech.dut.safefood.controller.shop;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import tech.dut.safefood.dto.response.APIResponse;
import tech.dut.safefood.dto.response.ReviewDetailResponseDto;
import tech.dut.safefood.dto.response.ReviewResponseDto;
import tech.dut.safefood.service.Shop.ShopCommunityService;
import tech.dut.safefood.vo.PageInfo;

@RestController
@RequestMapping("/shop/community")
@Validated
@RequiredArgsConstructor
public class ShopCommunityController {
    private static final String SHOP_GET_ALL_REVIEW = "/reviews";
    private static final String SHOP_DELETE_REVIEW = "/review/{id}";
    private static final String SHOP_LIKE_OR_DISLIKE_REVIEW_ENDPOINT = "/review/{id}/favorite";
    private static final String SHOP_SAVE_REPLY_ENDPOINT = "/review/{id}/replies";
    private static final String SHOP_LIKE_OR_DISLIKE_REPLY_ENDPOINT = "/review/reply/{id}/favorite";
    private static final String SHOP_DELETE_REPLY_ENDPOINT = "/review/reply/{id}";
    private static final String SHOP_GET_DETAIL_REVIEW_ENDPOINT = "/review/{id}";

    private final ShopCommunityService shopCommunityService;

    @ApiOperation("Shop like or dislike review")
    @PostMapping(SHOP_LIKE_OR_DISLIKE_REVIEW_ENDPOINT)
    public APIResponse<?> shopLikeOrDislikeReview(@PathVariable Integer id) {
        shopCommunityService.shopLikeOrDislikeReview(id);
        return APIResponse.okStatus();
    }

    @ApiOperation("Shop like or dislike reply")
    @PostMapping(SHOP_LIKE_OR_DISLIKE_REPLY_ENDPOINT)
    public APIResponse<?> shopLikeOrDislikeReply(@PathVariable Integer id) {
        shopCommunityService.shopLikeOrDislikeReply(id);
        return APIResponse.okStatus();
    }

    @ApiOperation("Shop delete review")
    @DeleteMapping(SHOP_DELETE_REVIEW)
    public APIResponse<?> shopDeleteReview(@PathVariable Integer id) {
        shopCommunityService.shopDeleteReview(id);
        return APIResponse.okStatus();
    }

    @ApiOperation("Shop delete reply")
    @DeleteMapping(SHOP_DELETE_REPLY_ENDPOINT)
    public APIResponse<?> shopDeleteReply(@PathVariable Integer id) {
        shopCommunityService.shopDeleteReply(id);
        return APIResponse.okStatus();
    }

    @ApiOperation("Shop get review detail")
    @GetMapping(SHOP_GET_DETAIL_REVIEW_ENDPOINT)
    public APIResponse<ReviewDetailResponseDto> shopGetReviewDetail(@PathVariable Long id) {
        return APIResponse.okStatus(shopCommunityService.shopGetReviewDetail(id));
    }

    @ApiOperation("Shop get all review")
    @GetMapping(SHOP_GET_ALL_REVIEW)
    public APIResponse<PageInfo<ReviewResponseDto>> shopGetAllReview(
            @ApiParam(value = "Limit")
            @RequestParam(required = false) Integer limit,
            @ApiParam(value = "Page")
            @RequestParam(required = false) Integer page) {
        return APIResponse.okStatus(shopCommunityService.shopGetAllReview(limit, page));
    }
}
