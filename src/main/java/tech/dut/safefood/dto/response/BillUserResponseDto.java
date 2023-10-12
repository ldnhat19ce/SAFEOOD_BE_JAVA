package tech.dut.safefood.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tech.dut.safefood.model.Address;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BillUserResponseDto implements Serializable {
    private Long id;
    private BigDecimal totalOrigin;
    private BigDecimal totalPayment;
    private BigDecimal totalDiscount;

    private Long userId;

    private String userFirstName;

    private String userLastName;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    private String status;

    private Long shopId;

    private String name;

    private String logo;

    private Long addressId;

    private String city;


    private String district;

    private String town;


    private String street;


    private Double x;

    private Double y;

    private Boolean isRating;

    private Double ratings;



    public BillUserResponseDto(Long id, BigDecimal totalOrigin, BigDecimal totalPayment, BigDecimal totalDiscount, Long userId, String userFirstName, String userLastName, LocalDateTime createdAt, String status) {
        this.id = id;
        this.totalOrigin = totalOrigin;
        this.totalPayment = totalPayment;
        this.totalDiscount = totalDiscount;
        this.userId = userId;
        this.userFirstName = userFirstName;
        this.userLastName = userLastName;
        this.createdAt = createdAt;
        this.status = status;
    }

    public BillUserResponseDto(Long id, BigDecimal totalOrigin, BigDecimal totalPayment, BigDecimal totalDiscount, LocalDateTime createdAt, String status, Long shopId, String name, String logo, Long addressId, String city, String district, String town, String street, Double x, Double y, Boolean isRating, Double ratings) {
        this.id = id;
        this.totalOrigin = totalOrigin;
        this.totalPayment = totalPayment;
        this.totalDiscount = totalDiscount;
        this.createdAt = createdAt;
        this.status = status;
        this.shopId = shopId;
        this.name = name;
        this.logo = logo;
        this.addressId = addressId;
        this.city = city;
        this.district = district;
        this.town = town;
        this.street = street;
        this.x = x;
        this.y = y;
        this.isRating = isRating;
        this.ratings = ratings;
    }
}
