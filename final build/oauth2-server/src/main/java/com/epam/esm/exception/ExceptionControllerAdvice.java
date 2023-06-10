package com.epam.esm.exception;


import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver;

import java.util.Arrays;
import java.util.List;

@ControllerAdvice
@RequiredArgsConstructor
public class ExceptionControllerAdvice extends ExceptionHandlerExceptionResolver {

    @ExceptionHandler(DuplicateKeyException.class)
    public ResponseEntity<ExceptionResponseBody> handleDuplicateEntityException(DuplicateKeyException ex) {
        ExceptionResponseBody build = new ExceptionResponseBody(40901, ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT.value()).body(build);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ExceptionResponseBody> handleEntityNotFoundException(EntityNotFoundException ex) {
        ExceptionResponseBody build = new ExceptionResponseBody(40401, ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND.value()).body(build);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<ExceptionResponseBody>> handleMethodArgumentNotValidException(
        MethodArgumentNotValidException ex) {
        List<ExceptionResponseBody> errors = ex.getBindingResult().getAllErrors().stream().map(error -> {
            String field = Arrays.stream(((FieldError) error).getField().split("\\."))
                .reduce((first, second) -> second)
                .orElse("field");
            String errorMessage = error.getDefaultMessage();
            Object value = ((FieldError) error).getRejectedValue();
            return errorBodyValidationMessageSetter(errorMessage, field, value);
        }).toList();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    private ExceptionResponseBody errorBodyValidationMessageSetter(String errorMessage, String errorField,
        Object invalidValue) {
        if (StringUtils.isNumeric(errorMessage)) {
            int errorCode = Integer.parseInt(errorMessage);
            return new ExceptionResponseBody(errorCode, String.format(errorMessage, invalidValue));
        }
        return new ExceptionResponseBody(40010, String.format("%s - %s", errorField, errorMessage));
    }

}
