package tech.dut.safefood.dto.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Getter
@Setter
public class PricePaymentCreateRequestDto implements Serializable {
    @NotNull
    private String billId;
}
