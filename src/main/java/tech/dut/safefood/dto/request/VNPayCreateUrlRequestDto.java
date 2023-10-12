package tech.dut.safefood.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VNPayCreateUrlRequestDto {
    private String billId;
    private String vnpReturnUrl;
    private String bankCode;
    private String vnpLocale;
    private String vnpOrderType;
}
