package tech.dut.safefood.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import tech.dut.safefood.util.constants.APIConstants;


public class APIResponse<T> extends ResponseEntity {
    public APIResponse(HttpStatus status, int code) {
        this(APIBody.builder().code(code).build(), null, status);
    }

    public APIResponse(T body, HttpStatus status, int code) {
        this(APIBody.builder().code(code).data(body).build(), null, status);
    }

    public APIResponse(APIBody body, MultiValueMap<String, String> headers, HttpStatus httpStatus) {
        super(body, headers, httpStatus);
    }

    public static <T> APIResponse<T> okStatus(T body) {
        return new APIResponse<T>(body, HttpStatus.OK, APIConstants.SUCCESS_CODE);
    }

    public static <T> APIResponse<T> okStatus() {
        return new APIResponse<T>(HttpStatus.OK, APIConstants.SUCCESS_CODE);
    }

    public static <T> APIResponse<T> errorStatus(T body, HttpStatus httpStatus) {
        return new APIResponse<T>(body, httpStatus, APIConstants.ERROR_CODE);
    }

    @Getter
    @Setter
    @Builder
    public static class APIBody<T> {
        int code;
        T data;
    }

}
