package tech.dut.safefood.dto.request;

import tech.dut.safefood.util.constants.APIConstants;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class ChangePasswordRequestDto {

    @NotEmpty(message = APIConstants.OLD_PASSWORD_NOT_EMPTY)
    private String oldPassword;

    @NotEmpty(message = APIConstants.ERROR_PASSWORD_NOT_EMPTY)
    private String newPassword;

    @NotEmpty(message = APIConstants.CONFIRM_NEW_PASSWORD_NOT_EMPTY)
    private String confirmNewPassword;
}
