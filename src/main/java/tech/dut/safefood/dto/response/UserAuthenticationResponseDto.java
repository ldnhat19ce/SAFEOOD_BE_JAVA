package tech.dut.safefood.dto.response;

import tech.dut.safefood.enums.UserEnum;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
public class UserAuthenticationResponseDto implements Serializable {

    @ApiModelProperty(readOnly = true)
    private String accessToken;

    @ApiModelProperty(readOnly = true)
    private String tokenType;

    @ApiModelProperty(readOnly = true)
    private String refreshToken;

    @ApiModelProperty(readOnly = true)
    private Long userId;

    @ApiModelProperty(readOnly = true)
    private Date expired;

    @Enumerated(EnumType.STRING)
    private UserEnum.Status status;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long digitCodeExpired;
}
