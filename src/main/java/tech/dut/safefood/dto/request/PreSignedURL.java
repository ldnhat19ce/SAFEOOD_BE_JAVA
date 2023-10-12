package tech.dut.safefood.dto.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.net.URL;

/**
 * A value object representing a pre-signed url
 */
@Getter
@Setter
public class PreSignedURL {
    @ApiModelProperty(readOnly = true)
    private URL preSignedURL;
    @ApiModelProperty(readOnly = true)
    private String url;
}
