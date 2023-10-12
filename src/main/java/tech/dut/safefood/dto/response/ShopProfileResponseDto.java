package tech.dut.safefood.dto.response;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class ShopProfileResponseDto implements Serializable {
    private Long id;
    private String name;
    private String logo;
    private String description;
    private String phone;
    private Double rantings;
    private List<String> imageShops;
    private String city;
    private String district;
    private String town;
    private String street;
    private Double x;
    private Double y;
    private Boolean isFauvorite;
}
