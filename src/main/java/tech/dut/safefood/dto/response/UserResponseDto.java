package tech.dut.safefood.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tech.dut.safefood.enums.AuthProvider;
import tech.dut.safefood.enums.UserEnum;

import java.io.Serializable;
import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDto implements Serializable {
    private Long id;
    private String email;
    private AuthProvider authProvider;
    private String phoneNumber;
    private UserEnum.Status status;
    private Instant createdAt;
    private Instant modifiedAt;
    private String Role;
}
