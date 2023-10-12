package tech.dut.safefood.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ReviewCreateResponseDto {
    private Long id;
    private String title;
    private String content;
    private List<String> images;
    private Long shopId;
}
