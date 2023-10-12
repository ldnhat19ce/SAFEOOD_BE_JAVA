package tech.dut.safefood.dto.request;

import lombok.Data;
import tech.dut.safefood.enums.ShopEnum;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class ProductRequestDto implements Serializable {

    private Long id;
    @Size(max = 255)
    @NotNull
    private String name;
    @Size(max = 255)
    private String image;
    @Size(max = 255)
    private String description;
    @NotNull
    private BigDecimal price;
    @Size(max = 50)
    private String status;

    @NotNull
    private Long categoryId;
}
