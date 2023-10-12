package tech.dut.safefood.dto.response;

import lombok.Data;

import java.util.List;

@Data
public class UserShopResponseDto {
    private  ShopProfileResponseDto shopProfile;

    private List<ProductResponseDto> listProduct;

    private List<VoucherResponseDto> listVoucher;
}
