package tech.dut.safefood.controller.shop;

import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tech.dut.safefood.dto.request.FeedBackShopRequestDto;
import tech.dut.safefood.dto.response.APIResponse;
import tech.dut.safefood.dto.response.ShopRatingsResponseDto;
import tech.dut.safefood.service.Shop.ShopRatingsService;

import java.util.List;

@RestController
@RequestMapping("/shop/ratings")
public class ShopRatingController {

    @Autowired
    private ShopRatingsService shopRatingsService;

    @ApiOperation("API for shop get all ratings")
    @GetMapping()
    public APIResponse<List<ShopRatingsResponseDto>> getAllRatings() {
        return APIResponse.okStatus(shopRatingsService.getAllRatingUser());
    }
}
