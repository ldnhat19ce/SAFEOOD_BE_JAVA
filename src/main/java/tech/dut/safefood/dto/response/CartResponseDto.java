package tech.dut.safefood.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CartResponseDto {
    private Long cartId;
    private Long productId;
    private String productName;
    private String banner;
    private BigDecimal totalPrice;
    private BigDecimal price;
    private Long amount;

    private Long shopId;
    private String nameShop;
    private String logo;

}
