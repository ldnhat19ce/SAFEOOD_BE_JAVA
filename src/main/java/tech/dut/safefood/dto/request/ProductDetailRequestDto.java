package tech.dut.safefood.dto.request;

import lombok.Data;

import java.io.Serializable;

@Data
public class ProductDetailRequestDto implements Serializable {
    private Long amount;
    private Long productId;
}
