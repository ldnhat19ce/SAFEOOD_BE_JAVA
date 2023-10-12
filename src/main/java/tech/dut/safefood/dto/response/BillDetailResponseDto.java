package tech.dut.safefood.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BillDetailResponseDto implements Serializable {
    private Long billId;
    private List<BillItemResponseDto> billItemResponseDtos;
    private BigDecimal totalOrigin;
    private BigDecimal totalPayment;
    private BigDecimal totalDiscount;
    private String paymentType;
    private String code;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime expiredDate;
    private String status;

    private Long userId;
    private String userFirstName;
    private String userLastName;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    private Long shopId;

    private String shopName;

    private String logo;

    private Long addressId;

    private String city;


    private String district;

    private String town;


    private String street;


    private Double x;

    private Double y;


    private Long voucherId;

    private String voucherName;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")

    private LocalDateTime voucherCreatedAt;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")

    private LocalDateTime voucherEndedAt;

    private String userType;

    private BigDecimal valueDiscount;

    private String voucherType;

    private BigDecimal valueNeed;

    private Boolean isRating;

    private Double rating;

    private String voucherDiscountImage;

}
