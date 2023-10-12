package tech.dut.safefood.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tech.dut.safefood.util.AppUtils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReviewResponseDto {
    private Long id;
    private String content;

    private String title;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    private Long totalFavorite;
    private Long totalReply;
    private Boolean isLiked = false;

    private List<String> images;

    List<ReplyResponseDto> reply;

    private CommonDto user;

    public ReviewResponseDto(Long id, String content, String title, Instant createdAt, long totalFavorite, long totalReply, boolean isLiked,
                             Long userId, String username, String imageUrl) {
        this.id = id;
        this.content = content;
        this.title = title;
        this.createdAt = AppUtils.instantToLocalDateTime(createdAt);
        this.totalFavorite = totalFavorite;
        this.totalReply = totalReply;
        this.isLiked = isLiked;
        this.user = new CommonDto(userId, username, imageUrl);
    }

    public ReviewResponseDto(IReviewResponseDto dto) {
        this.id = dto.getId();
        this.content = dto.getContent();
        this.title = dto.getTitle();
        this.createdAt = AppUtils.instantToLocalDateTime(dto.getCreatedAt());
        this.totalFavorite = dto.getTotalFavorite();
        this.totalReply = dto.getTotalReply();
        this.isLiked = dto.getLiked() > 0;
        this.user = new CommonDto(dto.getUserId(), dto.getUsername(), dto.getImageUrl());
    }
}
