package tech.dut.safefood.dto.request;

import tech.dut.safefood.util.constants.APIConstants;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Data
public class ForgotPasswordRequestDto {

    @Email(message = "Email invalid")
    @NotBlank(message = APIConstants.ERROR_EMAIL_NOT_BLANK)
    @NotEmpty(message = APIConstants.ERROR_EMAIL_NOT_EMPTY)
    private String email;
}
