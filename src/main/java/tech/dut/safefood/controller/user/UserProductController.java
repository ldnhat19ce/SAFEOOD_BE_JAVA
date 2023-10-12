package tech.dut.safefood.controller.user;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tech.dut.safefood.dto.response.APIResponse;
import tech.dut.safefood.dto.response.ProductResponseDto;
import tech.dut.safefood.service.User.UserProductService;
import tech.dut.safefood.vo.PageInfo;

import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/user/products")
public class UserProductController {
    @Autowired
    private UserProductService userProductService;

    @ApiOperation("API for user get Top Product")
    @GetMapping("/shop/{shopId}")
    public APIResponse<PageInfo<ProductResponseDto>> getTopProductShop(@ApiParam(value = "Page")
                                                                       @RequestParam(required = false) Integer page,
                                                                       @ApiParam(value = "Limit")
                                                                       @RequestParam(required = false) Integer limit,
                                                                       @PathVariable("shopId") Long shopId,
                                                                       @ApiParam(value = "Search by name")
                                                                           @RequestParam(required = false) String query)
    {
        return APIResponse.okStatus(userProductService.getTopProductShop(page,limit,shopId, query));
    }

    @ApiOperation("API for user get detail product")
    @GetMapping("/shop/detail/{id}")
    public APIResponse<ProductResponseDto> getDetailProduct(@NotNull @PathVariable("id") Long id) {
        return APIResponse.okStatus(userProductService.getDetail(id));
    }

    @ApiOperation("API for user get all products")
    @GetMapping(path = "/{shopId}")
    public APIResponse<PageInfo<ProductResponseDto>> getAllProducts(@ApiParam(value = "Page")
                                                                    @RequestParam(required = false) Integer page,
                                                                    @ApiParam(value = "Limit")
                                                                    @RequestParam(required = false) Integer limit,
                                                                    @ApiParam(value = "Status")
                                                                    @PathVariable("shopId") Long shopId,
                                                                    @ApiParam(value = "Search by name")
                                                                        @RequestParam(required = false) String query) {
        return APIResponse.okStatus(this.userProductService.getProductShop(page, limit, shopId, query));
    }
}
