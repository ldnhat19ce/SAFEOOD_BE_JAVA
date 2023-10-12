package tech.dut.safefood.dto.request;

import lombok.Data;

import java.util.List;

@Data
public class VoucherProductRequestDto {
    private Long shopId;
    private List<ProductDetailRequestDto> orderDetails;
}
