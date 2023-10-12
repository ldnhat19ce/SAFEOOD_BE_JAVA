package tech.dut.safefood.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TurnoverTotalResponseDto implements Serializable {
   private BigDecimal totalPriceProductOrigin;
   private BigDecimal totalPriceDiscount;
   private BigDecimal totalPrice;
}
