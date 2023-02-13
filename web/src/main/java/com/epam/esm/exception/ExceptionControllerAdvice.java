package com.epam.esm.exception;

import com.epam.esm.service.exception.IncorrectUpdateValueException;
import org.apache.commons.lang.StringUtils;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DuplicateKeyException;
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
     * Handles {@link DaoException}
     */
    @ExceptionHandler(DaoException.class)
    ResponseEntity<ErrorBody> handleDaoException(DaoException ex) {
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
                    ErrorBody errorBody = errorBodyValidationMessageSetter(errorMessage, field);
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
                    ErrorBody errorBody = errorBodyValidationMessageSetter(errorMessage, field);
                    errors.add(errorBody);
                });
        return ResponseEntity.badRequest().body(errors);
    }

    private ErrorBody errorBodyValidationMessageSetter(String errorMessage, String errorField) {
        ErrorBody errorBody = new ErrorBody();
        if (StringUtils.isNumeric(errorMessage)) {
            int errorCode = Integer.parseInt(errorMessage);
            errorBody.setErrorCode(errorCode);
            errorMessage = ExceptionMessageI18n.toLocale(String.valueOf(errorCode));
            errorBody.setErrorMessage(errorMessage);
        } else {
            errorBody.setErrorMessage(String.format("%s - %s", errorField, errorMessage));
            errorBody.setErrorCode(ErrorCodes.VALIDATION_ERROR);
        }
        return errorBody;
    }

    /**
     * Handles {@link DuplicateKeyException}
     */
    @ExceptionHandler
    public ResponseEntity<ErrorBody> handleDuplicateKeyException(DuplicateKeyException ex) {
        String errorMessage = ExceptionMessageI18n.toLocale("40010");
        ErrorBody errorBody = new ErrorBody(errorMessage, 40010);
        return ResponseEntity.status(HttpStatus.valueOf(errorBody.getErrorCode() / 100)).body(errorBody);
    }

    /**
     * Handles {@link IncorrectUpdateValueException}
     */
    @ExceptionHandler
    public ResponseEntity<ErrorBody> handleIncorrectUpdateValueException(IncorrectUpdateValueException ex) {
        String errorMessage = ExceptionMessageI18n.toLocale(String.valueOf(ex.getErrorCode()));
        ErrorBody errorBody = new ErrorBody(errorMessage, ex.getErrorCode());
        return ResponseEntity.status(HttpStatus.valueOf(errorBody.getErrorCode() / 100)).body(errorBody);
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
