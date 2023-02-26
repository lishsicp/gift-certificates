package com.epam.esm.exception;

import com.epam.esm.service.exception.ErrorCodes;
import com.epam.esm.service.exception.PersistenceException;
import org.apache.commons.lang.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@ControllerAdvice
public class ExceptionControllerAdvice {

    private final ExceptionMessageI18n exceptionMessageI18n;

    public ExceptionControllerAdvice(ExceptionMessageI18n exceptionMessageI18n) {
        this.exceptionMessageI18n = exceptionMessageI18n;
    }

    @ExceptionHandler(PersistenceException.class)
    ResponseEntity<ErrorBody> handlePersistenceException(PersistenceException ex) {
        String errorMessage = exceptionMessageI18n.toLocale(String.valueOf(ex.getErrorCode()));
        if (ex.getParameter() != null) errorMessage = String.format(errorMessage, ex.getParameter());
        ErrorBody errorBody = new ErrorBody(errorMessage, ex.getErrorCode());
        return ResponseEntity.status(HttpStatus.valueOf(errorBody.getErrorCode() / 100)).body(errorBody);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<Object> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        List<ErrorBody> errors = new ArrayList<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
                    String field = Arrays.stream(((FieldError) error).getField().split("\\.")).reduce((first, second) -> second).orElse("Field");
                    String errorMessage = error.getDefaultMessage();
                    ErrorBody errorBody = errorBodyValidationMessageSetter(errorMessage, field);
                    errors.add(errorBody);
                }
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    @ExceptionHandler
    public ResponseEntity<List<ErrorBody>> handleConstraintViolationException(ConstraintViolationException ex) {
        List<ErrorBody> errors = new ArrayList<>();
        ex.getConstraintViolations()
                .forEach(e -> {
                    String errorMessage = e.getMessage();
                    String field = e.getPropertyPath().iterator().next().getName();
                    ErrorBody errorBody = errorBodyValidationMessageSetter(errorMessage, field);
                    errors.add(errorBody);
                });
        return ResponseEntity.badRequest().body(errors);
    }

    private ErrorBody errorBodyValidationMessageSetter(String errorMessage, String errorField) {
        int errorCode;
        if (StringUtils.isNumeric(errorMessage)) {
            errorCode = Integer.parseInt(errorMessage);
            errorMessage = exceptionMessageI18n.toLocale(String.valueOf(errorCode));
        } else {
            errorMessage = String.format("%s - %s", errorField, errorMessage);
            errorCode = ErrorCodes.VALIDATION_ERROR;
        }
        return new ErrorBody(errorMessage, errorCode);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<Object> handleHttpRequestMethodNotSupportedException() {
        String errorMessage = exceptionMessageI18n.toLocale("error.methodNotSupported");
        ErrorBody errorBody = new ErrorBody(errorMessage, HttpStatus.METHOD_NOT_ALLOWED.value());
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(errorBody);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Object> handleMethodArgumentTypeMismatchException() {
        String errorMessage = exceptionMessageI18n.toLocale("error.badRequest");
        ErrorBody errorBody = new ErrorBody(errorMessage, HttpStatus.BAD_REQUEST.value());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorBody);
    }
}
