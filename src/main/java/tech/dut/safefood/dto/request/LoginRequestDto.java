package tech.dut.safefood.dto.request;

import tech.dut.safefood.util.constants.APIConstants;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

@Data
public class LoginRequestDto implements Serializable {

    @Email(message = APIConstants.ERROR_EMAIL_INVALID)
    @NotEmpty(message = APIConstants.ERROR_EMAIL_NOT_EMPTY)
    private String email;

    @NotEmpty(message = APIConstants.ERROR_PASSWORD_NOT_EMPTY)
    private String password;

    private String deviceToken;
}
