package tech.dut.safefood.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VNPayReturnUrlResponseDto {
    private String responseCode;
    private String message;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String signData;
}

