package tech.dut.safefood.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tech.dut.safefood.enums.CommonEnum;

@Getter
@Setter
@NoArgsConstructor
public class NotificationDto {
    private Integer id;
    private String title;
    private String content;
    private CommonEnum.NotificationType type;
    private String typeName;

    public NotificationDto(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
