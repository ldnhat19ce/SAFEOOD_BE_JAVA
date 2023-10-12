package tech.dut.safefood.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * A DTO for the {@link tech.dut.safefood.model.Product} entity
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponseDto implements Serializable {
    
    private  Long id;

    private  String name;

    private  String image;

    private  String description;

    private  BigDecimal price;

    private  String status;

    private  String categoryName;

    private Long categoryId;

    private Long countPay;

    private Long shopId;

    private String shopName;

}

