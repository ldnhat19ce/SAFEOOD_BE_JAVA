package tech.dut.safefood.controller.shop;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tech.dut.safefood.dto.CategoryDto;
import tech.dut.safefood.dto.response.APIResponse;
import tech.dut.safefood.service.Shop.ShopCategoryService;
import tech.dut.safefood.vo.PageInfo;

@RestController
@RequestMapping(path = "/shop/categories")
public class ShopCategoryController {
    @Autowired
    private ShopCategoryService shopCategoryService;
    @ApiOperation("API for get all categories")
    @GetMapping
    public APIResponse<PageInfo<CategoryDto>> getAllCategories(@ApiParam(value = "Page")
                                                               @RequestParam(required = false) Integer page,
                                                               @ApiParam(value = "Limit")
                                                               @RequestParam(required = false) Integer limit) {
        return APIResponse.okStatus(this.shopCategoryService.getAllCategory(page, limit));
    }
}
