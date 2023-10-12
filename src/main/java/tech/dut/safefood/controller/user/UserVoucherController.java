package tech.dut.safefood.controller.user;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tech.dut.safefood.dto.request.VoucherProductRequestDto;
import tech.dut.safefood.dto.response.APIResponse;
import tech.dut.safefood.dto.response.ShopFauvoriteResponseDto;
import tech.dut.safefood.dto.response.VoucherResponseDto;
import tech.dut.safefood.dto.response.VoucherUserResponseDto;
import tech.dut.safefood.service.User.UserVoucherService;
import tech.dut.safefood.vo.PageInfo;

import java.util.List;

@RestController
@RequestMapping("/user/vouchers")
public class UserVoucherController {

    @Autowired
    private UserVoucherService userVoucherService;

    @ApiOperation("API for user get all voucher for bill")
    @PostMapping("/bill")
    public APIResponse<List<VoucherUserResponseDto>> getAllVouchersForBill(@RequestBody VoucherProductRequestDto voucherProductRequestDto ) {
        return APIResponse.okStatus(userVoucherService.getAllVoucherForBill(voucherProductRequestDto));
    }

    @ApiOperation("API for user get detail voucher shop")
    @GetMapping("/{id}")
    public APIResponse<VoucherResponseDto> getDetail(@PathVariable("id") Long id) {
        return APIResponse.okStatus(userVoucherService.getDetailVoucher(id));
    }

    @ApiOperation("API for user get all voucher shop")
    @GetMapping("/shop/{id}")
    public APIResponse<PageInfo<VoucherResponseDto>> getAllVoucherShop(@ApiParam(value = "Page")
                                                                       @RequestParam(required = false) Integer page,
                                                                   @ApiParam(value = "Limit")
                                                                       @RequestParam(required = false) Integer limit,
                                                                   @ApiParam(value = "Search by name")
                                                                       @RequestParam(required = false) String query,@PathVariable("id") Long id) {
        return APIResponse.okStatus(userVoucherService.getShopVoucher(page, limit, id, query));
    }

    @ApiOperation("API for user get all voucher")
    @GetMapping
    public APIResponse<PageInfo<VoucherUserResponseDto>> getAllVoucher(@ApiParam(value = "Page")
                                                                       @RequestParam(required = false) Integer page,
                                                                   @ApiParam(value = "Limit")
                                                                       @RequestParam(required = false) Integer limit,
                                                                   @ApiParam(value = "Search by name")
                                                                       @RequestParam(required = false) String query, @RequestParam(value = "userType", required = false) String userType) {
        return APIResponse.okStatus(userVoucherService.getAllVoucher(page, limit, userType, query));
    }

    @ApiOperation("API for top voucher all shop")
    @GetMapping("/voucher-shop")
    public APIResponse<PageInfo<VoucherResponseDto>> getTopVoucherShop(@ApiParam(value = "Page")
                                                                           @RequestParam(required = false) Integer page,
                                                                       @ApiParam(value = "Limit")
                                                                           @RequestParam(required = false) Integer limit,
                                                                       @ApiParam(value = "Search by name")
                                                                           @RequestParam(required = false) String query) {
        return APIResponse.okStatus(userVoucherService.getTopVoucherAllShop(page,limit,query));
    }

    @ApiOperation("API for top voucher admin")
    @GetMapping("/voucher-admin")
    public APIResponse<PageInfo<VoucherResponseDto>> getTopVoucherAdmin(@ApiParam(value = "Page")
                                                                            @RequestParam(required = false) Integer page,
                                                                        @ApiParam(value = "Limit")
                                                                            @RequestParam(required = false) Integer limit,
                                                                        @ApiParam(value = "Search by name")
                                                                            @RequestParam(required = false) String query) {
        return APIResponse.okStatus(userVoucherService.getTopVoucherAdmin(page,limit,query));
    }
}
