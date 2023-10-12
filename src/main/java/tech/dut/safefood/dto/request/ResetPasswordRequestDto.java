package tech.dut.safefood.dto.request;

import tech.dut.safefood.util.constants.APIConstants;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Data
public class ResetPasswordRequestDto {
    @NotBlank(message = APIConstants.ERROR_AUTH_TOKEN_NOT_BLANK)
    @NotEmpty(message = APIConstants.ERROR_TOKEN_NOT_NULL)
    private String authToken;

    @NotBlank(message = APIConstants.ERROR_PASSWORD_NOT_BLANK)
    @NotEmpty(message = APIConstants.ERROR_PASSWORD_NOT_EMPTY)
    private String newPassword;
}
