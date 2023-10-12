package tech.dut.safefood.controller.user;

import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import tech.dut.safefood.dto.request.BillProductRequestDto;
import tech.dut.safefood.dto.request.BillResquestDto;
import tech.dut.safefood.dto.response.*;
import tech.dut.safefood.enums.BillEnum;
import tech.dut.safefood.service.User.UserBillService;

import java.util.List;

@RestController
@RequestMapping("/user/bills")
@Validated
public class UserBillController {

    @Autowired
    private UserBillService userBillService;

    @ApiOperation("API for user create bill")
    @PostMapping(path ="/create")
    public APIResponse<BillResponseDto> createBill(@RequestBody BillProductRequestDto billProductRequestDto) {
        return APIResponse.okStatus(userBillService.createBill(billProductRequestDto));
    }

    @ApiOperation("API for user all list bill")
    @GetMapping
    public APIResponse<List<BillUserResponseDto>> getAllListBill(@RequestParam BillEnum.Status status) {
        return APIResponse.okStatus(userBillService.getAllListBill(status));
    }

    @ApiOperation("API for user all get detail bill")
    @GetMapping("/{id}")
    public APIResponse<BillDetailResponseDto> getDetailBill(@PathVariable("id") Long id) {
        return APIResponse.okStatus(userBillService.getDetailBill(id));
    }
}
