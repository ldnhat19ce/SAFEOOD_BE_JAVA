package tech.dut.safefood.dto.request;

import lombok.Data;

import java.util.List;

@Data
public class BillProductRequestDto {
    private Long shopId;
    private Long VoucherId;
    private List<ProductDetailRequestDto> productDetailRequestDtos;
}
