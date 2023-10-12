package tech.dut.safefood.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tech.dut.safefood.util.AppUtils;

import java.time.Instant;
import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class ReplyResponseDto {
    private Long id;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime modifiedAt;
    private CommonDto user;
    private String content;
    private Long totalFavorite;
    private Boolean isLiked = false;
    private Long reviewId;

    public ReplyResponseDto(Long id, Instant createdAt, Instant modifiedAt, Long userId, String userName, String userImage, String content, Long totalFavorite, Boolean isLiked) {
        this.id = id;
        this.createdAt = AppUtils.instantToLocalDateTime(createdAt);
        this.modifiedAt = AppUtils.instantToLocalDateTime(modifiedAt);
        this.user = new CommonDto(userId, userName, userImage);
        this.content = content;
        this.totalFavorite = totalFavorite;
        this.isLiked = isLiked;
    }

    public ReplyResponseDto(Long id, Instant createdAt, Instant modifiedAt, Long userId, String userName, String userImage, String content, Long totalFavorite,
                            Boolean isLiked, Long reviewId) {
        this.id = id;
        this.createdAt = AppUtils.instantToLocalDateTime(createdAt);
        this.modifiedAt = AppUtils.instantToLocalDateTime(modifiedAt);
        this.user = new CommonDto(userId, userName, userImage);
        this.content = content;
        this.totalFavorite = totalFavorite;
        this.isLiked = isLiked;
        this.reviewId = reviewId;
    }
}
