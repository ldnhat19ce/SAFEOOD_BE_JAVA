package tech.dut.safefood.dto.request;

import lombok.Data;

import java.io.Serializable;

@Data
public class BillResquestDto implements Serializable {
    private Long voucherId;
}
