package tech.dut.safefood.dto;

import tech.dut.safefood.enums.UserEnum;
import tech.dut.safefood.util.constants.APIConstants;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
public class SignUpUserDto {

    @NotEmpty(message = APIConstants.ERROR_USERNAME_NOT_EMPTY)
    @ApiModelProperty(required = true)
    private String firstname;

    @NotEmpty(message = APIConstants.ERROR_USERNAME_NOT_EMPTY)
    @ApiModelProperty(required = true)
    private String lastname;

    @NotEmpty(message = APIConstants.ERROR_PASSWORD_NOT_EMPTY)
    @ApiModelProperty(required = true)
    private String password;

    private Long birthDay;

    @Email(message = APIConstants.ERROR_EMAIL_INVALID)
    @NotEmpty(message = APIConstants.ERROR_EMAIL_NOT_EMPTY)
    @ApiModelProperty(required = true)
    private String email;

    @Size(min = 10, max = 10, message = APIConstants.ERROR_PHONE_NUMBER_INVALID)
    private String phoneNumber;

    @ApiModelProperty(readOnly = true)
    private UserEnum.Status status;
}
