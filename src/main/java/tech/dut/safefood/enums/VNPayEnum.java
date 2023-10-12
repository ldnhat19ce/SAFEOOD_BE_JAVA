package tech.dut.safefood.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

public class VNPayEnum {
    @Getter
    @AllArgsConstructor
    public enum PaymentStatus {
        SUCCESS("00", "Thanh toan thanh cong"),
        UNKNOW_ERR("99", "Loi khong xac dinh"),
        INVALID_CHECKSUM("97", "Chu ki khong hop le"),
        ALREADY_CONFIRM("02", "Order da duoc thanh toan"),
        INVALID_AMOUNT("04", "So tien khong hop le"),
        ORDER_NOT_FOUND("01", "Order khong tim thay");

        private final String code;
        private final String message;
    }
}

