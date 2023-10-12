package tech.dut.safefood.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BillItemResponseDto {
    private String nameProduct;
    private Long amount;
    private BigDecimal price;
    private String nameShop;
    private String nameCategory;
    private String image;
}
