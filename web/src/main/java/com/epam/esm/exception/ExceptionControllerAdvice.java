package com.epam.esm.exception;

import com.epam.esm.service.exception.ExceptionErrorCode;
import com.epam.esm.service.exception.PersistentException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.validation.ConstraintViolationException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Handles exceptions from controller.
 * @author Yaroslav Lobur
 * @version 1.0
 */
@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ExceptionControllerAdvice {


    /**
     * Handles {@link PersistentException}
     */
    @ExceptionHandler(PersistentException.class)
    ResponseEntity<ErrorBody> handlePersistentException(PersistentException ex) {
        String errorMessage = ExceptionMessageI18n.toLocale(String.valueOf(ex.getErrorCode()));
        if (ex.getParameter() != null) errorMessage = String.format(errorMessage, ex.getParameter());
        ErrorBody errorBody = new ErrorBody(errorMessage, ex.getErrorCode());
        return ResponseEntity.status(HttpStatus.valueOf(errorBody.getErrorCode() / 100)).body(errorBody);
    }

    /**
     * Handles exception from {@link javax.validation.Valid} annotation
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<Object> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        List<ErrorBody> errors = new LinkedList<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
                    String field = Arrays.stream(((FieldError) error).getField().split("\\.")).reduce((first, second) -> second).orElse("Field");
                    String errorMessage = error.getDefaultMessage();
                    Object value = ((FieldError) error).getRejectedValue();
                    ErrorBody errorBody = errorBodyValidationMessageSetter(errorMessage, field, value);
                    errors.add(errorBody);
                }
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    /**
     * Handles exception from {@link Validated} annotation
     */
    @ExceptionHandler
    public ResponseEntity<List<ErrorBody>> handleConstraintViolationException(ConstraintViolationException ex) {
        List<ErrorBody> errors = new LinkedList<>();
        ex.getConstraintViolations()
                .forEach(e -> {
                    String errorMessage = e.getMessage();
                    String field = e.getPropertyPath().iterator().next().getName();
                    String invalidValue = "";
                    for (Object o : e.getPropertyPath()) {
                        invalidValue = o.toString();
                    }
                    ErrorBody errorBody = errorBodyValidationMessageSetter(errorMessage, field, invalidValue);
                    errors.add(errorBody);
                });
        return ResponseEntity.badRequest().body(errors);
    }

    private ErrorBody errorBodyValidationMessageSetter(String errorMessage, String errorField, Object invalidValue) {
        ErrorBody errorBody = new ErrorBody();
        if (StringUtils.isNumeric(errorMessage)) {
            int errorCode = Integer.parseInt(errorMessage);
            errorBody.setErrorCode(errorCode);
            errorMessage = ExceptionMessageI18n.toLocale(String.valueOf(errorCode));
            errorBody.setErrorMessage(String.format(errorMessage, invalidValue));
        } else {
            errorBody.setErrorMessage(String.format("%s - %s", errorField, errorMessage));
            errorBody.setErrorCode(ExceptionErrorCode.VALIDATION_ERROR);
        }
        return errorBody;
    }

    /**
     * Handles {@link HttpRequestMethodNotSupportedException}
     */
    @ExceptionHandler
    public ResponseEntity<Object> handle(HttpRequestMethodNotSupportedException ex) {
        String errorMessage = ExceptionMessageI18n.toLocale("error.methodNotSupported");
        ErrorBody errorBody = new ErrorBody(errorMessage, HttpStatus.METHOD_NOT_ALLOWED.value());
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(errorBody);
    }

    /**
     * Handles {@link MethodArgumentTypeMismatchException}
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Object> handle() {
        String errorMessage = ExceptionMessageI18n.toLocale("error.badRequest");
        ErrorBody errorBody = new ErrorBody(errorMessage, HttpStatus.BAD_REQUEST.value());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorBody);
    }
}
