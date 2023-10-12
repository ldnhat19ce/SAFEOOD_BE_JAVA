package tech.dut.safefood.controller.admin;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import tech.dut.safefood.dto.CategoryDto;
import tech.dut.safefood.dto.request.AdminNewsDTO;
import tech.dut.safefood.dto.request.VoucherRequestDto;
import tech.dut.safefood.dto.request.VoucherUpdateRequestDto;
import tech.dut.safefood.dto.response.*;
import tech.dut.safefood.enums.UserEnum;
import tech.dut.safefood.model.News;
import tech.dut.safefood.service.Admin.AdminManagerNewsService;
import tech.dut.safefood.service.Admin.AdminManagerProductService;
import tech.dut.safefood.service.Admin.AdminManagerService;
import tech.dut.safefood.service.Admin.AdminManagerVoucherService;
import tech.dut.safefood.vo.PageInfo;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping(path = "/admin/manager")
public class AdminManagerController {

    @Autowired
    private AdminManagerService adminManagerService;

    @Autowired
    private AdminManagerProductService adminManagerProductService;

    @Autowired
    private AdminManagerVoucherService adminManagerVoucherService;

    @Autowired
    private AdminManagerNewsService adminManagerNewsService;

    @ApiOperation("API for admin create voucher")
    @PostMapping(path = "/vouchers/create")
    public APIResponse<?> createVoucher(@RequestBody @Valid VoucherRequestDto voucherRequestDto) {
        adminManagerVoucherService.createVoucher(voucherRequestDto);
        return APIResponse.okStatus();
    }

    @ApiOperation("API for admin delete voucher")
    @DeleteMapping(path = "/vouchers/delete/{id}")
    public APIResponse<?> deleteVoucher(@NotNull @PathVariable("id") Long id) {
        adminManagerVoucherService.deleteVoucher(id);
        return APIResponse.okStatus();
    }

    @ApiOperation("API for admin update voucher")
    @PutMapping(path = "/vouchers/update/{id}")
    public APIResponse<?> deleteVoucher(@NotNull @PathVariable("id") Long id, @RequestBody @Valid VoucherUpdateRequestDto voucherRequestDto) {
        adminManagerVoucherService.updateVoucher(voucherRequestDto,id);
        return APIResponse.okStatus();
    }

    @ApiOperation("API for admin get detail sh voucher")
    @GetMapping(path = "/vouchers/detail/{id}")
    public APIResponse<VoucherResponseDto> getDetailVoucher(@NotNull @PathVariable("id") Long id) {
        return APIResponse.okStatus(adminManagerVoucherService.getDetailVoucher(id));
    }

    @ApiOperation("API for admin get  detail shop voucher")
    @GetMapping(path = "/vouchers/shops/detail/{id}")
    public APIResponse<VoucherResponseDto> getAllVoucher(@PathVariable("id") Long id) {
        return APIResponse.okStatus(adminManagerVoucherService.getDetailVoucherShop(id));
    }

    @ApiOperation("API for get all categories")
    @GetMapping(path = "/categories")
    public APIResponse<PageInfo<CategoryDto>> getAllCategories(@ApiParam(value = "Page")
                                                              @RequestParam(required = false) Integer page,
                                                          @ApiParam(value = "Limit")
                                                              @RequestParam(required = false) Integer limit,
                                                               @ApiParam(value = "Search by name")
                                                                   @RequestParam(required = false) String query) {
        return APIResponse.okStatus(this.adminManagerProductService.getAllCategory(page, limit, query));
    }

    @ApiOperation("API for get detail")
    @GetMapping(path = "/categories/{id}")
    public APIResponse<CategoryDto> getDetailCategory(@NotNull @PathVariable("id") Long id){
        return APIResponse.okStatus(this.adminManagerProductService.getDetailCategory(id));
    }


    @ApiOperation("API for Admin delete category")
    @DeleteMapping("/category/delete/{id}")
    public APIResponse<?> deleteCategory(@NotNull @PathVariable("id") Long id) {
        adminManagerProductService.deleteCategory(id);
        return APIResponse.okStatus();
    }

    @ApiOperation("API for Admin update category")
    @PutMapping("/category/update")
    public APIResponse<?> updateCategory(@Valid @RequestBody CategoryDto categoryDto) {
        adminManagerProductService.updateCategory(categoryDto);
        return APIResponse.okStatus();
    }

    @ApiOperation("API for Admin add category")
    @PostMapping("/category/add")
    public APIResponse<?> createCategory(@Valid @RequestBody CategoryDto categoryDto) {
        adminManagerProductService.createCategory(categoryDto);
        return APIResponse.okStatus();
    }

    @ApiOperation("API for get all users")
    @GetMapping(path = "/users")
    public APIResponse<PageInfo<UserResponseDto>> getAllUser(@ApiParam(value = "Page")
                                                                 @RequestParam(required = false) Integer page,
                                                             @ApiParam(value = "Limit")
                                                                 @RequestParam(required = false) Integer limit,
                                                             @ApiParam(value = "Search by name")
                                                                 @RequestParam(required = false) String query,
                                                             @ApiParam(value = "Search by status")
                                                                 @RequestParam(required = false) UserEnum.Status status ) {
        return APIResponse.okStatus(adminManagerService.getAllListUser(page, limit, query, status));
    }

    @ApiOperation("API for get all shops")
    @GetMapping(path = "/shops")
    public APIResponse<PageInfo<ShopResponseDto>> getAllShop(@ApiParam(value = "Page")
                                                             @RequestParam(required = false) Integer page,
                                                             @ApiParam(value = "Limit")
                                                             @RequestParam(required = false) Integer limit,
                                                             @ApiParam(value = "Status")
                                                                 @RequestParam(required = false) UserEnum.Status status,
                                                             @ApiParam(value = "Search by name")
                                                                 @RequestParam(required = false) String query) {
        return APIResponse.okStatus(adminManagerService.getAllShops(page, limit, status, query));
    }

    @ApiOperation("Block User")
    @DeleteMapping(path = "/users/block/{id}")
    public APIResponse<?> blockUser(@PathVariable("id") Long id) {
        adminManagerService.blockUser(id);
        return APIResponse.okStatus();
    }

    @ApiOperation("Active block User")
    @PatchMapping(path = "/users/active-block/{id}")
    public APIResponse<?> activeBlockUser(@PathVariable("id") Long id) {
        adminManagerService.rollBackBlockUser(id);
        return APIResponse.okStatus();
    }

    @ApiOperation("API for active Shop")
    @PatchMapping(path = "/shops/active/{id}")
    public APIResponse<?> activeShop(@PathVariable("id") Long id) {
        adminManagerService.activeShop(id);
        return APIResponse.okStatus();
    }

    @ApiOperation("API for block Shop")
    @PatchMapping(path = "/shops/block/{id}")
    public APIResponse<?> blockShop(@PathVariable("id") Long id) {
        adminManagerService.blockShop(id);
        return APIResponse.okStatus();
    }

    @ApiOperation("API for get all vouchers of Shop")
    @GetMapping(path = "/vouchers/shops/{id}")
    public APIResponse<List<VoucherResponseDto>> getAllVouchersShop(@ApiParam(value = "Page")
                                                                    @RequestParam(required = false) Integer page,
                                                                    @ApiParam(value = "Limit")
                                                                    @RequestParam(required = false) Integer limit, @PathVariable("id") Long id,
                                                                        @ApiParam(value = "Search by name")
                                                                            @RequestParam(required = false) String query,
                                                                        @ApiParam(value = "Search by status")
                                                                            @RequestParam(required = false) String status) {
        return APIResponse.okStatus(adminManagerVoucherService.getAllVoucherShop(page, limit,id, query, status));
    }

    @ApiOperation("API for get all vouchers admin")
    @GetMapping(path = "/vouchers")
    public APIResponse<List<VoucherResponseDto>> getAllVouchers(@ApiParam(value = "Page")
                                                                    @RequestParam(required = false) Integer page,
                                                                    @ApiParam(value = "Limit")
                                                                    @RequestParam(required = false) Integer limit,  @ApiParam(value = "Search by name")
                                                                        @RequestParam(required = false) String query,
                                                                    @ApiParam(value = "Search by status")
                                                                        @RequestParam(required = false) String status) {
        return APIResponse.okStatus(adminManagerVoucherService.getAllVoucher(page, limit, query, status));
    }

    @ApiOperation("API for get detail shop")
    @GetMapping(path = "/shops/{id}")
    public APIResponse<ShopDetailResponseDto> getDetailShops(@PathVariable("id") Long id) {
        return APIResponse.okStatus(adminManagerService.getDetailShop(id));
    }

    @ApiOperation("API for get all products")
    @GetMapping(path = "/products/shops/{id}")
    public APIResponse<PageInfo<ProductResponseDto>> getAllProducts(@ApiParam(value = "Page")
                                                                    @RequestParam(required = false) Integer page,
                                                                    @ApiParam(value = "Limit")
                                                                    @RequestParam(required = false) Integer limit,
                                                                    @PathVariable("id") Long id,
                                                                    @ApiParam(value = "Search by name")
                                                                        @RequestParam(required = false) String query) {
        return APIResponse.okStatus(adminManagerProductService.getAllProducts(page, limit, id, query));
    }


    @ApiOperation("Admin create news")
    @PostMapping(path = "/news/create")
    public APIResponse<AdminNewsDTO> adminCreateNews(@Valid @RequestBody AdminNewsDTO adminNewsDTO) {

        return APIResponse.okStatus(adminManagerNewsService.adminCreateNews(adminNewsDTO));
    }

    @ApiOperation("Admin update news")
    @PutMapping(path = "/news/update")
    public APIResponse<AdminNewsDTO> adminUpdateNews(@Valid @RequestBody AdminNewsDTO adminNewsDTO) {
        return APIResponse.okStatus(adminManagerNewsService.adminUpdateNews(adminNewsDTO));
    }

    @ApiOperation("Admin get page news")
    @GetMapping(path ="/news")
    public APIResponse<PageInfo<AdminNewsDTO>> adminUpdateNews(@ApiParam(value = "Page") @RequestParam(required = false) Integer page,
                                                       @ApiParam(value = "Limit") @RequestParam(required = false) Integer limit,
                                                       @ApiParam(value = "Search by name")
                                                           @RequestParam(required = false) String query) {
        return APIResponse.okStatus(adminManagerNewsService.adminGetPageNews(page, limit, query));
    }

    @ApiOperation("Admin delete news by id")
    @DeleteMapping(path = "/news/delete/{id}")
    public APIResponse<?> adminDeleteNews(@PathVariable Long id) {
        adminManagerNewsService.adminDeleteNews(id);
        return APIResponse.okStatus();
    }

    @ApiOperation("Admin get detail news by id")
    @GetMapping("/news/detail/{id}")
    public APIResponse<AdminNewsDTO> adminGetDetailNews(@PathVariable Long id) {
        return APIResponse.okStatus(adminManagerNewsService.adminGetDetailNewsById(id));
    }
}
