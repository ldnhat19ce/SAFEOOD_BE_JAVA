package tech.dut.safefood.dto.response;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShopLocationResponseDTO {
    private Long id;
    private String name;
    private String description;
    private String logo;
    private Double x;
    private Double y;
    private String street;
    private Double distance;
    private Double ratings;

    public ShopLocationResponseDTO(Long id, String name, String description, String logo, Double x, Double y, String street, Double ratings) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.logo = logo;
        this.x = x;
        this.y = y;
        this.street = street;
        this.ratings = ratings;
    }

}
