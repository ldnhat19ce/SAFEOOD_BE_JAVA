package tech.dut.safefood.dto.request;

import lombok.Data;
import tech.dut.safefood.enums.VoucherEnum;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;

/**
 * A DTO for the {@link tech.dut.safefood.model.Voucher} entity
 */
@Data
public class VoucherRequestDto {
    private Long id;
    @Size(max = 50)
    @NotNull
    private  String name;
    @NotNull
    private Long endedAt;
    @NotNull
    private  BigDecimal valueDiscount;
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

    @NotNull
    private Long createdAt;

    private String description;
}