package tech.dut.safefood.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BillResponseDto  implements Serializable {
    private Long billId;
    private List<ProductResponseDto> productResponseDtos;
    private Long code;
    private String typePayment;
    private String statusPayment;
    private LocalDateTime expiredDate;
    private BigDecimal totalOrigin;
    private BigDecimal totalPayment;
    private BigDecimal totalDiscount;
}
