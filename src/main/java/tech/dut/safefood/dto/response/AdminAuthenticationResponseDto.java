package tech.dut.safefood.dto.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

@Data
@RequiredArgsConstructor
public class AdminAuthenticationResponseDto implements Serializable {
    @ApiModelProperty(value = "Access token", readOnly = true)
    private String token;
    @ApiModelProperty(value = "Access token expired date", readOnly = true)
    private Date expired;

    @ApiModelProperty(value = "Refresh token")
    @NotNull(message = "Refresh token {validation.not-null}")
    @NotEmpty(message = "Refresh token {validation.not-empty}")
    private String refreshToken;

    @ApiModelProperty(value = "User Id", readOnly = true)
    private Long userId;

    @ApiModelProperty(readOnly = true)
    private String tokenType;
}
