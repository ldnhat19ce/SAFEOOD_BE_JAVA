package tech.dut.safefood.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartUpdateRequestDto implements Serializable {
    private Long cartId;
    private Long amount;
}
