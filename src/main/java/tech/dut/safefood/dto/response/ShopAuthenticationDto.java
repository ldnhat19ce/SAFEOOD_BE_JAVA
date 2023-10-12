package tech.dut.safefood.dto.response;

import tech.dut.safefood.enums.UserEnum;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.Date;

@Data
public class ShopAuthenticationDto {
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
