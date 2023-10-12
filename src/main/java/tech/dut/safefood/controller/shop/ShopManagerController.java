package tech.dut.safefood.controller.shop;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import tech.dut.safefood.dto.request.ProductRequestDto;
import tech.dut.safefood.dto.request.VoucherRequestDto;
import tech.dut.safefood.dto.request.VoucherUpdateRequestDto;
import tech.dut.safefood.dto.response.APIResponse;
import tech.dut.safefood.dto.response.ProductResponseDto;
import tech.dut.safefood.dto.response.VoucherResponseDto;
import tech.dut.safefood.enums.VoucherEnum;
import tech.dut.safefood.service.Shop.ShopManagerProductService;
import tech.dut.safefood.service.Shop.ShopManagerVoucherService;
import tech.dut.safefood.vo.PageInfo;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping("/shop/manager")
@Validated
public class ShopManagerController {

    @Autowired
    private ShopManagerProductService shopManagerProductService;

    @Autowired
    private ShopManagerVoucherService shopManagerVoucherService;

    @ApiOperation("API for Shop add product")
    @PostMapping("/product")
    public APIResponse<?> createProduct(@Valid @RequestBody ProductRequestDto productRequestDto) {
        shopManagerProductService.createProduct(productRequestDto);
        return APIResponse.okStatus();
    }

    @ApiOperation("API for get all products")
    @GetMapping(path = "/products")
    public APIResponse<PageInfo<ProductResponseDto>> getAllProducts(@ApiParam(value = "Page")
                                                                 @RequestParam(required = false) Integer page,
                                                                 @ApiParam(value = "Limit")
                                                                 @RequestParam(required = false) Integer limit,
                                                                    @ApiParam(value = "Status")
                                                                        @RequestParam(required = false) String status,
                                                                    @ApiParam(value = "Search by name")
                                                                        @RequestParam(required = false) String query) {
        return APIResponse.okStatus(this.shopManagerProductService.getAllProducts(page, limit, status, query));
    }

    @ApiOperation("API for Shop delete product")
    @DeleteMapping("/product/delete/{id}")
    public APIResponse<?> deleteProduct(@NotNull @PathVariable("id") Long id) {
        shopManagerProductService.deleteProduct(id);
        return APIResponse.okStatus();
    }

    @ApiOperation("API for Shop update product")
    @PutMapping("/product/update")
    public APIResponse<?> updateProduct(@Valid @RequestBody ProductRequestDto productRequestDto) {
        shopManagerProductService.updateProduct(productRequestDto);
        return APIResponse.okStatus();
    }

    @ApiOperation("API for Shop get detail product")
    @GetMapping("/product/detail/{id}")
    public APIResponse<ProductResponseDto> getDetailProduct(@NotNull @PathVariable("id") Long id) {
        return APIResponse.okStatus(shopManagerProductService.getDetail(id));
    }


    @ApiOperation("API for Shop add voucher")
    @PostMapping("/voucher")
    public APIResponse<?> createVoucher(@Valid @RequestBody VoucherRequestDto voucherRequestDto) {
        shopManagerVoucherService.createVoucher(voucherRequestDto);
        return APIResponse.okStatus();
    }

    @ApiOperation("API for get all vouchers")
    @GetMapping(path = "/vouchers")
    public APIResponse<List<VoucherResponseDto>> getAllVouchers(@ApiParam(value = "Page")
                                                                    @RequestParam(required = false) Integer page,
                                                                @ApiParam(value = "Limit")
                                                                    @RequestParam(required = false) Integer limit,
                                                                @ApiParam(value = "Search by name")
                                                                        @RequestParam(required = false) String query,
                                                                @ApiParam(value = "Search by status")
                                                                        @RequestParam(required = false) String status) {
        return APIResponse.okStatus(this.shopManagerVoucherService.getAllVoucher(page, limit, query, status));
    }

    @ApiOperation("API for Shop delete voucher")
    @DeleteMapping("/voucher/delete/{id}")
    public APIResponse<?> deleteVoucher(@NotNull @PathVariable("id") Long id) {
        shopManagerVoucherService.deleteVoucher(id);
        return APIResponse.okStatus();
    }

    @ApiOperation("API for Shop update voucher")
    @PutMapping("/voucher/update")
    public APIResponse<?> updateVoucher(@Valid @RequestBody VoucherUpdateRequestDto voucherRequestDto) {
        shopManagerVoucherService.updateVoucher(voucherRequestDto);
        return APIResponse.okStatus();
    }

    @ApiOperation("API for Shop get detail voucher")
    @GetMapping("/voucher/detail/{id}")
    public APIResponse<VoucherResponseDto> getDetailVoucher(@NotNull @PathVariable("id") Long id) {
        return APIResponse.okStatus(shopManagerVoucherService.getDetail(id));
    }

}
