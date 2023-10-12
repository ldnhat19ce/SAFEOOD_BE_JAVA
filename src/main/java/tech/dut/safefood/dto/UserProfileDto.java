package tech.dut.safefood.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserProfileDto implements Serializable {
    private Long userInformationId;
    private String firstName;
    private String lastName;
    private String gender;
    private Instant birthday;
    private String userImage;
}
