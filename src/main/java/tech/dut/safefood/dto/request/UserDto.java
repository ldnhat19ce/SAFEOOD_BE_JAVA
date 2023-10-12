package tech.dut.safefood.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Objects;

@Data
@AllArgsConstructor
public class UserDto {
    private Long userId;
    private String deviceToken;
    private String username;
    private Boolean isLogin;

    public UserDto(Long userId, String deviceToken, String username) {
        this.userId = userId;
        this.deviceToken = deviceToken;
        this.username = username;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserDto userDTO = (UserDto) o;
        return Objects.equals(userId, userDTO.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId);
    }
}
