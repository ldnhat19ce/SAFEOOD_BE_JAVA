package tech.dut.safefood.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tech.dut.safefood.enums.CommonEnum;
import tech.dut.safefood.util.AppUtils;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.Instant;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NotificationResponseDto {
    private String title;
    private String content;
    private Boolean seenFlag = false;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
    private Integer typeId;

    @Enumerated(EnumType.STRING)
    private CommonEnum.NotificationType type;


    public NotificationResponseDto(String title, String content, Boolean seenFlag, Instant createdAt, Integer typeId, CommonEnum.NotificationType type) {
        this.title = title;
        this.content = content;
        this.seenFlag = seenFlag;
        this.createdAt = AppUtils.instantToLocalDateTime(createdAt);
        this.typeId = typeId;
        this.type = type;
    }
}
