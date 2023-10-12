package tech.dut.safefood.controller.shop;

import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;
import tech.dut.safefood.dto.response.APIResponse;
import tech.dut.safefood.dto.response.BillDetailResponseDto;
import tech.dut.safefood.dto.response.BillUserResponseDto;
import tech.dut.safefood.enums.BillEnum;
import tech.dut.safefood.service.Shop.ShopBillService;
import tech.dut.safefood.service.User.UserBillService;

import java.util.List;

@RestController
@RequestMapping("/shops/bills")
public class ShopBillController {

    @Autowired
    private ShopBillService shopBillService;

    @ApiOperation("API for shop all list bill")
    @GetMapping
    public APIResponse<List<BillUserResponseDto>> getAllListBill(@RequestParam BillEnum.Status status) {
        return APIResponse.okStatus(shopBillService.getAllListBill(status));
    }

    @ApiOperation("API for user shop get detail bill")
    @GetMapping("/{id}")
    public APIResponse<BillDetailResponseDto> getDetailBill(@PathVariable("id") Long id) {
        return APIResponse.okStatus(shopBillService.getDetailBill(id));
    }

    @ApiOperation("API for user shop get detail bill by code")
    @GetMapping("/code")
    public APIResponse<BillDetailResponseDto> getBillByCode(@RequestParam("code") String code) {
        return APIResponse.okStatus(shopBillService.getBillByCode(code));
    }

    @ApiOperation("API for user shop done bill")
    @PatchMapping("/done/{id}")
    public APIResponse<?> getBillByCode(@PathVariable("id") Long billId) {
        shopBillService.doneBill(billId);
        return APIResponse.okStatus();
    }
}
