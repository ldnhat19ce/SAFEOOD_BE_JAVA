package tech.dut.safefood.configurator.oauth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FacebookAccountDto {
    private String id;
    private String name;
    private String firstName;
    private String lastName;
    private Map<String, Object> picture;
    private String email;
}
