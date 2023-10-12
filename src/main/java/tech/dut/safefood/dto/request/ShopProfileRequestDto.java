package tech.dut.safefood.dto.request;

import lombok.Data;

import java.io.Serializable;

@Data
public class ShopProfileRequestDto implements Serializable {
    private String description;
    private String logo;
    private String status;
}
