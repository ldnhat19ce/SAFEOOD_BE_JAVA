package tech.dut.safefood.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OAuth2RequestDto {
    private String accessToken;
    private String tokenType;
    private String deviceToken;
}
