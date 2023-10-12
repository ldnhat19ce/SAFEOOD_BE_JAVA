package tech.dut.safefood.dto.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tech.dut.safefood.util.constants.APIConstants;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReviewRequestDto {
    private Long shopId;

    @NotBlank(message = APIConstants.ERROR_REVIEW_CONTENT_NOT_BLANK)
    @NotNull(message = APIConstants.ERROR_REVIEW_CONTENT_NOT_NULL)
    @ApiModelProperty(required = true)
    private String content;

    @NotBlank(message = APIConstants.ERROR_REVIEW_TITLE_NOT_BLANK)
    @NotNull(message = APIConstants.ERROR_REVIEW_TITLE_NOT_NULL)
    @ApiModelProperty(required = true)
    private String title;

    private List<String> reviewImages;
}
