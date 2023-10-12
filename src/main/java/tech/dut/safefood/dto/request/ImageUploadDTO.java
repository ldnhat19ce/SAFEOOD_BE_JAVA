package tech.dut.safefood.dto.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import tech.dut.safefood.enums.CommonEnum;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * A DTO representing a image information
 */
@Getter
@Setter
public class ImageUploadDTO {
    @NotEmpty(message = "File name is required")
    @NotNull(message = "File name is required")
    @ApiModelProperty(notes = "File name", required = true)
    private String fileName;

    @NotEmpty(message = "File type is required")
    @NotNull(message = "File type is required")
    @ApiModelProperty(notes = "File type", required = true)
    private String fileType;

    @NotEmpty(message = "Image folder type is not null")
    @ApiModelProperty(notes = "Image folder type", required = true)
    private CommonEnum.ImageFolderType imageFolderType;
}