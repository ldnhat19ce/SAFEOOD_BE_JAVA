package tech.dut.safefood.controller.shop;

import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tech.dut.safefood.dto.response.APIResponse;
import tech.dut.safefood.dto.response.TopProductResponseDto;
import tech.dut.safefood.dto.response.TurnoverResponseDto;
import tech.dut.safefood.service.Shop.ShopManagerTurnoverService;
import tech.dut.safefood.vo.PageInfo;

import java.util.List;

@RestController
@RequestMapping("/shop/turnover")
@Validated
public class ShopTurnoverController {

    @Autowired
    private ShopManagerTurnoverService shopManagerTurnoverService;

    @ApiOperation("API for Shop turnover Revenue")
    @GetMapping("/revenue")
    public APIResponse<TurnoverResponseDto> getRevenue(@RequestParam("dateFrom") Long dateFrom, @RequestParam("dateTo") Long dateTo) {
        return APIResponse.okStatus(shopManagerTurnoverService.getRevenue(dateFrom, dateTo));
    }

    @ApiOperation("API for Shop turnover product")
    @GetMapping("/product")
    public APIResponse<List<TopProductResponseDto>> getTopProduct(@RequestParam("dateFrom") Long dateFrom, @RequestParam("dateTo") Long dateTo, @RequestParam("limit") Integer limit) {
        return APIResponse.okStatus(shopManagerTurnoverService.getTopProduct(dateFrom, dateTo, limit));
    }
}
