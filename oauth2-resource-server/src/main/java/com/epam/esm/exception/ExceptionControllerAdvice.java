package com.epam.esm.exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@ControllerAdvice
@RequiredArgsConstructor
public class ExceptionControllerAdvice extends ExceptionHandlerExceptionResolver {

    private final ExceptionMessageI18n exceptionMessageI18n;

    @ExceptionHandler(PersistentException.class)
    ResponseEntity<ErrorBody> handlePersistentException(PersistentException ex) {
        ErrorBody errorBody = getErrorBody(ex);
        return ResponseEntity.status(HttpStatus.valueOf(errorBody.getErrorCode() / 100)).body(errorBody);
    }

    private ErrorBody getErrorBody(PersistentException ex) {
        String errorMessage = exceptionMessageI18n.toLocale(String.valueOf(ex.getErrorCode()));
        if (ex.getParameter() != null) {
            errorMessage = String.format(errorMessage, ex.getParameter());
        }
        return new ErrorBody(errorMessage, ex.getErrorCode());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<List<ErrorBody>> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        List<ErrorBody> errors = ex.getBindingResult().getAllErrors().stream()
            .map(error -> {
                String field = Arrays.stream(((FieldError) error).getField().split("\\."))
                    .reduce((first, second) -> second)
                    .orElse("Field");
                String errorMessage = error.getDefaultMessage();
                Object value = ((FieldError) error).getRejectedValue();
                return errorBodyValidationMessageSetter(errorMessage, field, value);
            })
            .toList();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    @ExceptionHandler
    public ResponseEntity<List<ErrorBody>> handleConstraintViolationException(ConstraintViolationException ex) {
        List<ErrorBody> errors = new ArrayList<>();
        ex.getConstraintViolations()
            .forEach(e -> {
                String field = e.getPropertyPath().iterator().next().getName();
                String invalidValue = "";
                for (Object o : e.getPropertyPath()) {
                    invalidValue = o.toString();
                }
                String errorMessage = e.getMessage();
                ErrorBody errorBody = errorBodyValidationMessageSetter(errorMessage, field, invalidValue);
                errors.add(errorBody);
            });
        return ResponseEntity.badRequest().body(errors);
    }

    private ErrorBody errorBodyValidationMessageSetter(String errorMessage, String errorField, Object invalidValue) {
        if (StringUtils.isNumeric(errorMessage)) {
            int errorCode = Integer.parseInt(errorMessage);
            errorMessage = exceptionMessageI18n.toLocale(String.valueOf(errorCode));
            return new ErrorBody(String.format(errorMessage, invalidValue), errorCode);
        }
        return new ErrorBody(String.format("%s - %s", errorField, errorMessage), ErrorCodes.VALIDATION_ERROR);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<Object> handleHttpRequestMethodNotSupportedException() {
        String errorMessage = exceptionMessageI18n.toLocale("error.methodNotSupported");
        ErrorBody errorBody = new ErrorBody(errorMessage, HttpStatus.METHOD_NOT_ALLOWED.value());
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(errorBody);
    }

    @ExceptionHandler({
        MethodArgumentTypeMismatchException.class,
        HttpMessageNotReadableException.class,
        JsonProcessingException.class
    })
    public ResponseEntity<Object> handle() {
        String errorMessage = exceptionMessageI18n.toLocale("error.badRequest");
        ErrorBody errorBody = new ErrorBody(errorMessage, HttpStatus.BAD_REQUEST.value());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorBody);
    }

    @ExceptionHandler(UnsupportedOperationException.class)
    public ResponseEntity<Object> handleUnsupportedOperationException() {
        String errorMessage = exceptionMessageI18n.toLocale("error.unsupportedOperation");
        ErrorBody errorBody = new ErrorBody(errorMessage, HttpStatus.METHOD_NOT_ALLOWED.value());
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(errorBody);
    }
}
