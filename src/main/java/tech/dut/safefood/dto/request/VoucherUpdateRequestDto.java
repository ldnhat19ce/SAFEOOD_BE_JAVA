package tech.dut.safefood.dto.request;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class VoucherUpdateRequestDto implements Serializable {

    @NotNull
    private Long id;
    private  String name;
    @NotNull
    private Long endedAt;
    @NotNull
    private BigDecimal valueDiscount;
    @NotNull
    private  String voucherType;
    @NotNull
    private BigDecimal valueNeed;
    @NotNull
    private  Long limitPerUser;
    @NotNull
    private  BigDecimal maxDiscount;
    @NotNull
    private Long quantity;

    @NotNull
    private String image;

    private String description;
}
