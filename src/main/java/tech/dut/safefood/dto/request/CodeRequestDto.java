package tech.dut.safefood.dto.request;

import tech.dut.safefood.util.constants.APIConstants;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

@Data
public class CodeRequestDto implements Serializable {
    @NotBlank(message = APIConstants.DIGIT_CODE_CAN_NOT_BLANK)
    @NotEmpty(message = APIConstants.DIGIT_CODE_CAN_NOT_EMPTY)
    private String code;
}
