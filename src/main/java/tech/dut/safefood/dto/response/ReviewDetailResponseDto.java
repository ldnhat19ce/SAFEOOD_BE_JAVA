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
@AllArgsConstructor
@NoArgsConstructor
public class ReviewDetailResponseDto {
    private Long id;
    private String content;
    private String title;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    private List<String> images;

    List<ReplyResponseDto> reply;

    private Long totalFavorite;
    private Long totalReply;
    private Boolean isLiked = false;

    private CommonDto user;
    private CommonDto shop;

    public ReviewDetailResponseDto(Long id, String content, String title, Instant createdAt, Long totalFavorite, Long totalReply, Boolean isLiked,
                                   Long userId, String username, String imageUrl, Long shopId, String shopName) {
        this.id = id;
        this.content = content;
        this.title = title;
        this.createdAt = AppUtils.instantToLocalDateTime(createdAt);
        this.totalFavorite = totalFavorite;
        this.totalReply = totalReply;
        this.isLiked = isLiked;
        this.user = new CommonDto(userId, username, imageUrl);
        this.shop = new CommonDto(shopId, shopName);
    }
}
