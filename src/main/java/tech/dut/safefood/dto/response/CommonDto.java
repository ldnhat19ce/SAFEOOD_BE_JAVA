package tech.dut.safefood.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CommonDto {
    private Long id;

    private String name;

    private String imageUrl;

    public CommonDto(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
