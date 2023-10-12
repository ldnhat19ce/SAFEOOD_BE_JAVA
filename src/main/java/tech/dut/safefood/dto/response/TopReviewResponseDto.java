package tech.dut.safefood.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TopReviewResponseDto {
    private Long reviewId;
    private Long shopId;
    private String title;
    private String content;
}
