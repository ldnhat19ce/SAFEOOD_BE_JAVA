package tech.dut.safefood.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TurnoverProductResponseDto implements Serializable {
    private Long id;
    private String productName;
    private Long quantity;
    private BigDecimal totalOriginPrice;
}
