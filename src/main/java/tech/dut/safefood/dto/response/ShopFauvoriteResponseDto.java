package tech.dut.safefood.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShopFauvoriteResponseDto implements Serializable {
    private Long id;
    private String name;
    private String banner;
    private String description;
    private String schedule;
    private String phone;
}
