package tech.dut.safefood.controller.user;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tech.dut.safefood.dto.request.BillResquestDto;
import tech.dut.safefood.dto.response.*;
import tech.dut.safefood.enums.UserEnum;
import tech.dut.safefood.service.User.UserShopService;
import tech.dut.safefood.vo.PageInfo;

import java.util.List;

@RestController
@RequestMapping("/users/shops")
public class UserShopController {

    @Autowired
    private UserShopService userShopService;

    @ApiOperation("API for user get detail shop")
    @GetMapping(path = "/detail/{shopId}")
    public APIResponse<UserShopResponseDto> getDetailShop(@PathVariable("shopId") Long shopId) {
        return APIResponse.okStatus(userShopService.getDetailShop(shopId));
    }

    @ApiOperation("API for Admin turnover Revenue Shop")
    @GetMapping("/top")
    public APIResponse<List<TurnoverTotalShopResponseDto>> getTopRevenue(@RequestParam("dateFrom") Long dateFrom, @RequestParam("dateTo") Long dateTo, @RequestParam("top") Integer top) {
        return APIResponse.okStatus(userShopService.getTopRevenue(dateFrom, dateTo, top));
    }


    @ApiOperation("API for user get distance near")
    @GetMapping("/distance")
    public APIResponse<PageInfo<ShopLocationResponseDTO>> getDistance(@RequestParam("x") Double x, @RequestParam("y") Double y, @RequestParam("radius") Double radius, @RequestParam(value = "limit", required = false) Integer limit, @RequestParam(value = "page", required = false) Integer page) {
        return APIResponse.okStatus(userShopService.getAllLocationByCurrentLocation(x, y, radius, limit, page));
    }

    @ApiOperation("API for shop get distance near")
    @GetMapping("/distance-shop")
    public APIResponse<PageInfo<ShopLocationResponseDTO>> getDistanceShop(@RequestParam("shopId") Long shopId, @RequestParam("radius") Double radius, @RequestParam(value = "limit", required = false) Integer limit, @RequestParam(value = "page", required = false) Integer page) {
        return APIResponse.okStatus(userShopService.getAllShopLocationByCurrentLocation(shopId, radius, limit, page));
    }

    @ApiOperation("API for get all shops")
    @GetMapping(path = "/shops")
    public APIResponse<PageInfo<ShopResponseDto>> getAllShop(@ApiParam(value = "Page")
                                                             @RequestParam(required = false) Integer page,
                                                             @ApiParam(value = "Limit")
                                                             @RequestParam(required = false) Integer limit,
                                                             @ApiParam(value = "Status")
                                                             @RequestParam(required = false) UserEnum.Status status,
                                                             @ApiParam(value = "Search by name")
                                                             @RequestParam(required = false) String query) {
        return APIResponse.okStatus(userShopService.getAllShops(page, limit, status, query));
    }

    @ApiOperation("API for create recent shop")
    @PostMapping(path = "/create/recent-shop")
    public APIResponse<?> createRecentShop(@ApiParam(value = "ShopId") @RequestParam(required = true) Long shopId) {
        userShopService.createRecentShop(shopId);
        return APIResponse.okStatus();
    }

    @ApiOperation("API for get all recent shop")
    @GetMapping(path = "/recent-shops")
    public APIResponse<List<ShopResponseDto>> getAllRecentShop() {
        return APIResponse.okStatus(userShopService.getRecentShop());
    }
}
