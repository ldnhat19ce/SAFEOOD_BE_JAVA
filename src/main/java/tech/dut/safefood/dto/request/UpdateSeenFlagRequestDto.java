package tech.dut.safefood.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UpdateSeenFlagRequestDto {
    private List<Long> notificationIds;
}
