package tech.dut.safefood.dto.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tech.dut.safefood.util.constants.APIConstants;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminNewsDTO {

    private Long id;

    @NotEmpty(message = APIConstants.ERROR_NEW_TITLE_NOT_NULL)
    @NotBlank(message = APIConstants.ERROR_NEW_TITLE_NOT_BLANK)
    @ApiModelProperty(required = true)
    private String title;

    private String latitude;

    private String address;

    @NotEmpty(message = APIConstants.ERROR_NEW_CONTENT_NOT_NULL)
    @NotBlank(message = APIConstants.ERROR_NEW_CONTENT_NOT_BLANK)
    @ApiModelProperty(required = true)
    private String content;

    private String image;

    private String subTitle;
}
