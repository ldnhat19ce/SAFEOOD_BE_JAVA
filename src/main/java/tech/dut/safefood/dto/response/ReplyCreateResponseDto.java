package tech.dut.safefood.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReplyCreateResponseDto {
    private Long id;
    private Long reviewId;
    private String content;
}
