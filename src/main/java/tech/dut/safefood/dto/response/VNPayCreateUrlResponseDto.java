package tech.dut.safefood.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VNPayCreateUrlResponseDto {
    private String url;
    private Integer billId;
}
