package tech.dut.safefood.controller.user;

import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import tech.dut.safefood.dto.request.PricePaymentCreateRequestDto;
import tech.dut.safefood.dto.request.VNPayCreateUrlRequestDto;
import tech.dut.safefood.dto.response.APIResponse;
import tech.dut.safefood.dto.response.VNPayCreateUrlResponseDto;
import tech.dut.safefood.dto.response.VNPayReturnUrlResponseDto;
import tech.dut.safefood.service.User.UserPricePaymentService;
import tech.dut.safefood.service.User.UserVNPAYService;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;

@RestController
@RequestMapping("/user/payment")
@Validated
public class UserVNPAYController {

    @Autowired
    private UserVNPAYService userVNPAYService;

    @Autowired
    private UserPricePaymentService userPricePaymentService;

    @ApiOperation("User payment with vn pay")
    @PostMapping(path = "/vnpay/payment-url")
    public APIResponse<VNPayCreateUrlResponseDto> createPaymentUrl(@RequestBody VNPayCreateUrlRequestDto requestDTO, HttpServletRequest request) throws Exception {
        return APIResponse.okStatus(userVNPAYService.createPaymentUrl(requestDTO, request));
    }

    @ApiOperation("Get return payment url")
    @GetMapping(path = "/vnpay/return-url")
    public APIResponse<VNPayReturnUrlResponseDto> getReturnPaymentUrl(HttpServletRequest request) throws UnsupportedEncodingException {
        return APIResponse.okStatus(userVNPAYService.getReturnPaymentUrl(request));
    }

    @ApiOperation("Get ipn url")
    @GetMapping(path = "/vnpay/ipn-url")
    public APIResponse<VNPayReturnUrlResponseDto> getIPNUrl(HttpServletRequest request) throws UnsupportedEncodingException {
        System.out.println(request.getQueryString());
        return APIResponse.okStatus(userVNPAYService.getIPNUrl(request));
    }

    @ApiOperation("Get payment price")
    @PostMapping(path = "/price")
    public APIResponse<?> createPricePayment(PricePaymentCreateRequestDto request) {
        userPricePaymentService.createPaymentPrice(request);
        return APIResponse.okStatus();
    }
}
