package tech.dut.safefood.dto.response;

import java.time.Instant;

public interface IReviewResponseDto {
    Long getId();
    String getContent();
    String getTitle();
    Instant getCreatedAt();
    Long getTotalFavorite();
    Long getTotalReply();
    long getLiked();
    Long getUserId();
    String getUsername();
    String getImageUrl();
}
