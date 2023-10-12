package tech.dut.safefood.configurator.oauth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GoogleAccountDto {
    private String id;
    private String name;
    private String given_name;
    private String family_name;
    private String picture;
    private String email;
}
