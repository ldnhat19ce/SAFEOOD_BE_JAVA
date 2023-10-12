package tech.dut.safefood.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tech.dut.safefood.util.AppUtils;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserNewsResponseDto {
    private Long newsId;
    private String title;
    private String location;
    private String address;
    private String content;
    private Long createdAt;
    private String newsImage;
    private String subTitle;

    public UserNewsResponseDto(Long newsId, String title, String location, String address, String content, LocalDateTime createdAt, String newsImage, String subTilte) {
        this.newsId = newsId;
        this.title = title;
        this.location = location;
        this.address = address;
        this.content = content;
        this.createdAt = AppUtils.convertLocalDateTimeToMilli(createdAt);
        this.newsImage = newsImage;
        this.subTitle = subTilte;
    }
}
