package tech.dut.safefood.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShopDetailResponseDto implements Serializable {
    private Long id;
    private String name;
    private String banner;
    private String description;
}
