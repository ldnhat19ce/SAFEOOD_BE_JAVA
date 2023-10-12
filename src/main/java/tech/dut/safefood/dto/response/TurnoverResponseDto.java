package tech.dut.safefood.dto.response;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class TurnoverResponseDto implements Serializable {
    private TurnoverTotalResponseDto turnoverTotalResponseDto;
    private List<TurnoverProductResponseDto> productResponseDto;
}
