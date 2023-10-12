package tech.dut.safefood.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tech.dut.safefood.enums.UserEnum;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShopResponseDto implements Serializable {
    private Long id;
    private String name;
    private UserEnum.Status status;
    private String description;
    private String phone;
    private String logo;
    private Double ratings;

    public ShopResponseDto(Long id, String name, String description, String phone, String logo, Double ratings) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.phone = phone;
        this.logo = logo;
        this.ratings = ratings;
    }
}