package tech.dut.safefood.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VoucherUserResponseDto {
    private Long id;

    private String name;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endedAt;

    private String userType;

    private String image;

    private BigDecimal valueDiscount;

    private String voucherType;

    private BigDecimal valueNeed;

    private Long limitPerUser;

    private BigDecimal maxDiscount;

    private Long quantity;

    private Long shopId;

    private String shopName;

    private String description;

    private Boolean deleteFlag;

    private String status;

    public VoucherUserResponseDto(Long id, String name, LocalDateTime createdAt, LocalDateTime endedAt, String userType, String image, BigDecimal valueDiscount, String voucherType, BigDecimal valueNeed, Long limitPerUser, BigDecimal maxDiscount, Long quantity, Long shopId, String shopName, String description, Boolean deleteFlag) {
        this.id = id;
        this.name = name;
        this.createdAt = createdAt;
        this.endedAt = endedAt;
        this.userType = userType;
        this.image = image;
        this.valueDiscount = valueDiscount;
        this.voucherType = voucherType;
        this.valueNeed = valueNeed;
        this.limitPerUser = limitPerUser;
        this.maxDiscount = maxDiscount;
        this.quantity = quantity;
        this.shopId = shopId;
        this.shopName = shopName;
        this.description = description;
        this.deleteFlag = deleteFlag;
    }
}
