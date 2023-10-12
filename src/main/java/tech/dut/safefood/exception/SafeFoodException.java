package tech.dut.safefood.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SafeFoodException extends RuntimeException {

    public static final String ERROR_INVALID_TOKEN = "ERROR_INVALID_TOKEN";

    public static final String ERROR_INVALID_ROLE = "ERROR_INVALID_ROLE";

    public static final String EMAIL_OR_PASSWORD_IS_INVALID = "EMAIL_OR_PASSWORD_IS_INVALID";

    public static final String DIGIT_CODE_INCORRECT = "DIGIT_CODE_INCORRECT";

    public static final String DIGIT_CODE_HAS_EXPIRED = "DIGIT_CODE_HAS_EXPIRED";

    public static final String ERROR_USER_NOT_FOUND = "ERROR_USER_NOT_FOUND";

    public static final String ERROR_CAN_NOT_SEND_EMAIL = "ERROR_CAN_NOT_SEND_EMAIL";

    public static final String ERROR_EMAIL_ALREADY_EXISTS = "ERROR_EMAIL_ALREADY_EXISTS";

    public static final String ERROR_PHONE_NUMBER_ALREADY_EXISTS = "ERROR_PHONE_NUMBER_ALREADY_EXISTS";
    public static final String PASSWORD_INPUT_IS_INVALID = "PASSWORD_INPUT_IS_INVALID";

    public static final String ERROR_CONFIRM_NEW_PASSWORD_NOT_MATCH = "ERROR_CONFIRM_NEW_PASSWORD_NOT_MATCH";
    public static final String ERROR_OLD_PASSWORD_NOT_MATCH = "ERROR_OLD_PASSWORD_NOT_MATCH";
    public static final String UNAUTHORIZED_INVALID_PASSWORD = "UNAUTHORIZED_INVALID_PASSWORD";

    public static final String USER_IS_INACTIVED = "USER_IS_INACTIVED";

    public static final String USER_IS_NOT_BLOCK = "USER_IS_NOT_BLOCK";

    public static final String ROLE_IS_NOT_SHOP = "ROLE_IS_NOT_SHOP";
    public static final String ERROR_SHOP_NOT_ACTIVE = "ERROR_SHOP_NOT_ACTIVE";
    public static final String ERROR_USER_NOT_IS_SHOP = "ERROR_USER_NOT_IS_SHOP";
    public static final String ERROR_SHOP_DONT_HAVE_THIS_PRODUCT = "ERROR_SHOP_DONT_HAVE_THIS_PRODUCT";
    public static final String ERROR_CATEGORY_IS_EXISTS = "ERROR_CATEGORY_IS_EXISTS";

    public static final String ERROR_CATEGORY_IS_NOT_EXISTS = "ERROR_CATEGORY_IS_NOT_EXISTS";
    public static final String ERROR_VOUCHER_IS_NOT_EXISTS = "ERROR_VOUCHER_IS_NOT_EXISTS";
    public static final String ERROR_PRODUCT_NOT_EXISTS = "ERROR_PRODUCT_NOT_EXISTS";
    public static final String ERROR_SHOP_NOT_FOUND = "ERROR_SHOP_NOT_FOUND";
    public static final String ERROR_USER_FAVOURITE_SHOP_NOT_FOUND = "ERROR_USER_FAVOURITE_SHOP_NOT_FOUND";
    public static final String ERROR_CART_NOT_EXISTS = "ERROR_CART_NOT_EXISTS";
    public static final String ERROR_CART_IS_EMPTY = "ERROR_CART_IS_EMPTY";
    public static final String ERROR_BILL_NOT_EXISTS = "ERROR_BILL_NOT_EXISTS";
    public static final String ERROR_VOUCHER_IS_EXPIRED = "ERROR_VOUCHER_IS_EXPIRED";
    public static final String ERROR_TOTAL_ORIGIN_LESS_VALUE_NEED = "ERROR_TOTAL_ORIGIN_LESS_VALUE_NEED";
    public static final String ERROR_VOUCHER_USER_NOT_EXISTS = "ERROR_VOUCHER_USER_NOT_EXISTS";
    public static final String ERROR_VOUCHER_IS_FULL = "ERROR_VOUCHER_IS_FULL";

    public static final String ERROR_VOUCHER_USER_IS_FULL = "ERROR_VOUCHER_USER_IS_FULL";
    public static final String ERROR_NEWS_NOT_FOUND = "ERROR_NEWS_NOT_FOUND";
    public static final String ERROR_USER_INFORMATION_NOT_FOUND = "ERROR_USER_INFORMATION_NOT_FOUND";
    public static final String ERROR_TYPE_VOUCHER_IS_NOT_CORRECT = "ERROR_TYPE_VOUCHER_IS_NOT_CORRECT";
    public static final String ERROR_SHOP_SCHEDULE_NOT_CORRECT = "ERROR_SHOP_SCHEDULE_NOT_CORRECT";
    public static final String ERROR_USERTYPE_NOT_CORRECT = "ERROR_USERTYPE_NOT_CORRECT";
    public static final String ERROR_VOUCHER_STATUS_IS_NOT_CORRECT = "ERROR_VOUCHER_STATUS_IS_NOT_CORRECT";
    public static final String ERROR_CREATEDAT_NOT_MORE_THAN_ENDEDAT = "ERROR_CREATEDAT_NOT_MORE_THAN_ENDEDAT" ;
    public static final String ERROR_REVIEW_NOT_FOUND = "ERROR_REVIEW_NOT_FOUND";
    public static final String ERROR_REPLY_NOT_FOUND = "ERROR_REPLY_NOT_FOUND";



    public SafeFoodException() {
    }

    public SafeFoodException(String message) {
        super(message);
    }

    public SafeFoodException(String message, Throwable cause) {
        super(message, cause);
    }

    public SafeFoodException(Throwable cause) {
        super(cause);
    }

    public SafeFoodException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
