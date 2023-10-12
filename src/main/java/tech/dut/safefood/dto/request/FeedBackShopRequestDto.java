package tech.dut.safefood.dto.request;

import lombok.Data;

import java.io.Serializable;

@Data
public class FeedBackShopRequestDto implements Serializable {
    private Long shopId;
    private Long billId;
    private Double start;
    private String content;
}
