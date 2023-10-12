package tech.dut.safefood.controller.user;

import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tech.dut.safefood.dto.request.FeedBackShopRequestDto;
import tech.dut.safefood.dto.response.APIResponse;
import tech.dut.safefood.dto.response.BillUserResponseDto;
import tech.dut.safefood.dto.response.ShopRatingsResponseDto;
import tech.dut.safefood.service.User.UserRatingShopService;

import java.util.List;

@RestController
@RequestMapping("/shops/ratings")
public class UserFeedBackShopController {
    @Autowired
    private UserRatingShopService userRatingShopService;

    @ApiOperation("API for user feedback shop")
    @PostMapping
    public APIResponse<?> feedBackShop(@RequestBody FeedBackShopRequestDto feedBackShopRequestDto) {
        userRatingShopService.feedBackStart(feedBackShopRequestDto);
        return APIResponse.okStatus();
    }

    @ApiOperation("API for user get all ratings shop")
    @GetMapping("/{shopId}")
    public APIResponse<List<ShopRatingsResponseDto>> getAllRatings(@PathVariable("shopId") Long shopId) {
        return APIResponse.okStatus(userRatingShopService.getAllRatingUser(shopId));
    }


}
