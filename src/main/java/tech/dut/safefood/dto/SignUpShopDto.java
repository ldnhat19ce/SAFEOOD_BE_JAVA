package tech.dut.safefood.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import tech.dut.safefood.enums.UserEnum;
import tech.dut.safefood.util.constants.APIConstants;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Data
public class SignUpShopDto implements Serializable {

    @NotEmpty(message = APIConstants.ERROR_NAME_NOT_EMPTY)
    @ApiModelProperty(required = true)
    private String name;


    @ApiModelProperty(required = true)
    private String banner;

    @ApiModelProperty(required = true)
    private String description;


    @NotEmpty(message = APIConstants.ERROR_PASSWORD_NOT_EMPTY)
    @ApiModelProperty(required = true)
    private String password;

    @Email(message = APIConstants.ERROR_EMAIL_INVALID)
    @NotEmpty(message = APIConstants.ERROR_EMAIL_NOT_EMPTY)
    @ApiModelProperty(required = true)
    private String email;

    @Size(min = 10, max = 10, message = APIConstants.ERROR_PHONE_NUMBER_INVALID)
    private String phoneNumber;

    @ApiModelProperty(required = true)
    private UserEnum.Status status;

    @ApiModelProperty(required = true)
    @NotEmpty(message = APIConstants.ERROR_CITY_NOT_EMPTY)
    private String city;

    @ApiModelProperty(required = true)
    @NotEmpty(message = APIConstants.ERROR_DISTRICT_NOT_EMPTY)
    private String district;

    @ApiModelProperty(required = true)
    @NotEmpty(message = APIConstants.ERROR_TOWN_NOT_EMPTY)
    private String town;

    @ApiModelProperty(required = true)
    @NotEmpty(message = APIConstants.ERROR_STREET_NOT_EMPTY)
    private String street;

    @ApiModelProperty(required = true)
    @NotNull(message = APIConstants.ERROR_X_NOT_EMPTY)
    private Double x;

    @ApiModelProperty(required = true)
    @NotNull(message = APIConstants.ERROR_Y_NOT_EMPTY)
    private Double y;

}
