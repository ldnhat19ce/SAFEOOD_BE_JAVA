package tech.dut.safefood.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartCreateRequestDto implements Serializable {
    private Long shopId;
    private Long productId;
    private Long userId;
    private Long amount;
}
