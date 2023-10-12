package tech.dut.safefood.controller.admin;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tech.dut.safefood.dto.response.APIResponse;
import tech.dut.safefood.dto.response.TurnoverResponseDto;
import tech.dut.safefood.dto.response.TurnoverTotalShopResponseDto;
import tech.dut.safefood.service.Admin.AdminTurnoverService;

import java.util.List;

@RestController
@RequestMapping(path = "/admin/turnover")
public class AdminTurnoverController {
    @Autowired
    private AdminTurnoverService adminTurnoverService;

    @ApiOperation("API for Admin turnover Revenue Shop")
    @GetMapping("/shops")
    public APIResponse<List<TurnoverTotalShopResponseDto>> getRevenue(@RequestParam("dateFrom") Long dateFrom, @RequestParam("dateTo") Long dateTo) {
        return APIResponse.okStatus(adminTurnoverService.getRevenue(dateFrom, dateTo));
    }


    @ApiOperation("API for Admin turnover Revenue Shop")
    @GetMapping("/shops/{shopId}")
    public APIResponse<TurnoverTotalShopResponseDto> getRevenue(@RequestParam("dateFrom") Long dateFrom, @RequestParam("dateTo") Long dateTo, @PathVariable("shopId") Long shopId) {
        return APIResponse.okStatus(adminTurnoverService.getRevenueShop(dateFrom, dateTo, shopId));
    }

    @ApiOperation("API for Admin turnover Revenue Shop")
    @GetMapping("/shops/top")
    public APIResponse<List<TurnoverTotalShopResponseDto>> getTop10Revenue(@RequestParam("dateFrom") Long dateFrom, @RequestParam("dateTo") Long dateTo) {
        return APIResponse.okStatus(adminTurnoverService.getTop10Revenue(dateFrom, dateTo));
    }
}
