package tech.dut.safefood.controller.user;

import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import tech.dut.safefood.dto.request.LoginRequestDto;
import tech.dut.safefood.dto.response.APIResponse;
import tech.dut.safefood.dto.response.ShopFauvoriteResponseDto;
import tech.dut.safefood.dto.response.UserAuthenticationResponseDto;
import tech.dut.safefood.service.User.UserFavoriteService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/user/fauvourite")
@Validated
public class UserFauvouriteController {

    @Autowired
    private UserFavoriteService userFavoriteService;

    @ApiOperation("API for user get all favourite shop")
    @GetMapping(path ="/shops")
    public APIResponse<List<ShopFauvoriteResponseDto>> getAllShopFauvorites() {
        return APIResponse.okStatus(userFavoriteService.getAllFauvoriteUsers());
    }

    @ApiOperation("API for user add favourite shop")
    @PostMapping(path ="/shops/add")
    public APIResponse<?> createShopFauvorite(@RequestParam("shopId") Long shopId) {
        userFavoriteService.createFavouriteShop(shopId);
        return APIResponse.okStatus();
    }

    @ApiOperation("API for user delete favourite shop")
    @DeleteMapping(path ="/shops/delete")
    public APIResponse<?> deleteShopFauvorite(@RequestParam("shopId") Long shopId) {
        userFavoriteService.deleteFavouriteShop(shopId);
        return APIResponse.okStatus();
    }
}
