package tech.dut.safefood.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TopProductResponseDto implements Serializable {
    private Long id;
    private String productName;
    private Long quantity;
    private String image;
    private BigDecimal price;
}
