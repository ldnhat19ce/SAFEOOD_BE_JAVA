package tech.dut.safefood.controller;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import tech.dut.safefood.dto.response.APIResponse;
import tech.dut.safefood.exception.SafeFoodException;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class SafeFoodExceptionController {
    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    public APIResponse<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        Map<String, String> errors = new HashMap<>();
        exception.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return APIResponse.errorStatus(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {UsernameNotFoundException.class})
    public APIResponse<?> handleUsernameNotFoundException(UsernameNotFoundException exception) {
        return APIResponse.errorStatus(exception.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(value = {BadCredentialsException.class})
    public APIResponse<?> handleUsernameNotFoundException(BadCredentialsException exception) {
        return APIResponse.errorStatus(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {SafeFoodException.class})
    public APIResponse<?> handleUsernameNotFoundException(SafeFoodException exception) {
        return APIResponse.errorStatus(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }

}
