package tech.dut.safefood.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MetaDataDto {
    private String typeId;
    private String type;
    private String typeName = "";
    private String url = "";
    private String title;
    private String content;

    public MetaDataDto(String typeId, String type, String typeName, String url) {
        this.typeId = typeId;
        this.type = type;
        this.typeName = typeName;
        this.url = url;
    }
}
