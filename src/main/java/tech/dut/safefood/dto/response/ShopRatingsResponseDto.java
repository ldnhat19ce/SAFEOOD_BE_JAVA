package tech.dut.safefood.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShopRatingsResponseDto {
    private Long userId;
    private String userFirstName;
    private String userLastName;
    private String userImage;

    private String content;
    private Double ratings;
}
