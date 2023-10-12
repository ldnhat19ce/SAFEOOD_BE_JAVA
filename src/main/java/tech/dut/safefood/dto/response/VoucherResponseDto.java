package tech.dut.safefood.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tech.dut.safefood.enums.VoucherEnum;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * A DTO for the {@link tech.dut.safefood.model.Voucher} entity
 */
@Data
@NoArgsConstructor
public class VoucherResponseDto implements Serializable {
    private Long id;
    private String name;

    private Long countUserVoucher;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endedAt;
    private String userType;
    private BigDecimal valueDiscount;
    private String voucherType;
    private BigDecimal valueNeed;
    private Long limitPerUser;
    private BigDecimal maxDiscount;
    private Long quantity;

    private Boolean deleteFlag;

    private String image;

    private String status;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long shopId;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String shopName;

    private String description;

    public VoucherResponseDto(Long id, String name, LocalDateTime createdAt, LocalDateTime endedAt, String userType, BigDecimal valueDiscount, String voucherType, BigDecimal valueNeed, Long limitPerUser, BigDecimal maxDiscount, Long quantity, Boolean deleteFlag, String image, String description, Long shopId, String shopName) {
        this.id = id;
        this.name = name;
        this.createdAt = createdAt;
        this.endedAt = endedAt;
        this.userType = userType;
        this.valueDiscount = valueDiscount;
        this.voucherType = voucherType;
        this.valueNeed = valueNeed;
        this.limitPerUser = limitPerUser;
        this.maxDiscount = maxDiscount;
        this.quantity = quantity;
        this.deleteFlag = deleteFlag;
        this.image = image;
        this.description = description;
    }

    public VoucherResponseDto(Long id, String name, LocalDateTime createdAt, LocalDateTime endedAt, String userType, BigDecimal valueDiscount, String voucherType, BigDecimal valueNeed, Long limitPerUser, BigDecimal maxDiscount, Long quantity, String image, String description, Long shopId, String shopName) {
        this.id = id;
        this.name = name;
        this.createdAt = createdAt;
        this.endedAt = endedAt;
        this.userType = userType;
        this.valueDiscount = valueDiscount;
        this.voucherType = voucherType;
        this.valueNeed = valueNeed;
        this.limitPerUser = limitPerUser;
        this.maxDiscount = maxDiscount;
        this.quantity = quantity;
        this.image = image;
        this.description = description;
        this.shopId = shopId;
        this.shopName = shopName;
    }

    public VoucherResponseDto(Long id, String name, LocalDateTime createdAt, LocalDateTime endedAt, String userType, BigDecimal valueDiscount, String voucherType, BigDecimal valueNeed, Long limitPerUser, BigDecimal maxDiscount, Long quantity, String image, String description, Long shopId, String shopName, Long countVoucher, Boolean delete_flag) {
        this.id = id;
        this.name = name;
        this.createdAt = createdAt;
        this.endedAt = endedAt;
        this.userType = userType;
        this.valueDiscount = valueDiscount;
        this.voucherType = voucherType;
        this.valueNeed = valueNeed;
        this.limitPerUser = limitPerUser;
        this.maxDiscount = maxDiscount;
        this.quantity = quantity;
        this.image = image;
        this.description = description;
        this.shopId = shopId;
        this.shopName = shopName;
        this.countUserVoucher = countVoucher;
        this.deleteFlag = delete_flag;
    }

    public VoucherResponseDto(Long id, String name, LocalDateTime createdAt, LocalDateTime endedAt, String userType, BigDecimal valueDiscount, String voucherType, BigDecimal valueNeed, Long limitPerUser, BigDecimal maxDiscount, Long quantity, String image, String description,  Long countVoucher, Boolean delete_flag) {
        this.id = id;
        this.name = name;
        this.createdAt = createdAt;
        this.endedAt = endedAt;
        this.userType = userType;
        this.valueDiscount = valueDiscount;
        this.voucherType = voucherType;
        this.valueNeed = valueNeed;
        this.limitPerUser = limitPerUser;
        this.maxDiscount = maxDiscount;
        this.quantity = quantity;
        this.image = image;
        this.description = description;
        this.countUserVoucher = countVoucher;
        this.deleteFlag = delete_flag;
    }

    public VoucherResponseDto(Long id, String name, LocalDateTime createdAt, LocalDateTime endedAt, String userType, BigDecimal valueDiscount, String voucherType, BigDecimal valueNeed, Long limitPerUser, BigDecimal maxDiscount, Long quantity, String image, String description, Boolean deleteFlag) {
        this.id = id;
        this.name = name;
        this.createdAt = createdAt;
        this.endedAt = endedAt;
        this.userType = userType;
        this.valueDiscount = valueDiscount;
        this.voucherType = voucherType;
        this.valueNeed = valueNeed;
        this.limitPerUser = limitPerUser;
        this.maxDiscount = maxDiscount;
        this.quantity = quantity;
        this.image = image;
        this.description = description;
        this.deleteFlag = deleteFlag;
    }

    public VoucherResponseDto(Long id, String name, LocalDateTime createdAt, LocalDateTime endedAt, String userType, BigDecimal valueDiscount, String voucherType, BigDecimal valueNeed, Long limitPerUser, BigDecimal maxDiscount, Long quantity, Boolean deleteFlag, String image, String description) {
        this.id = id;
        this.name = name;
        this.createdAt = createdAt;
        this.endedAt = endedAt;
        this.userType = userType;
        this.valueDiscount = valueDiscount;
        this.voucherType = voucherType;
        this.valueNeed = valueNeed;
        this.limitPerUser = limitPerUser;
        this.maxDiscount = maxDiscount;
        this.quantity = quantity;
        this.deleteFlag = deleteFlag;
        this.image = image;
        this.description = description;
    }
}