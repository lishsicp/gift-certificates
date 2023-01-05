package com.epam.esm.exception;

import com.epam.esm.service.exception.IncorrectUpdateValueException;
import org.apache.commons.lang.StringUtils;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.validation.ConstraintViolationException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ExceptionErrorBodyControllerAdvice {

    @ExceptionHandler(DaoException.class)
    ResponseEntity<ErrorBody> handleDaoException(DaoException ex) {
        String errorMessage = ExceptionMessageLocalizer.toLocale(String.valueOf(ex.getErrorCode()));
        ErrorBody errorBody = new ErrorBody(errorMessage, ex.getErrorCode());
        return ResponseEntity.status(HttpStatus.valueOf(errorBody.getErrorCode() / 100)).body(errorBody);
    }

    /**
     * Handles exception from <code>javax.validation.Valid</code> annotation
     * */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<Object> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        List<ErrorBody> errors = new LinkedList<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
                    ErrorBody errorBody = new ErrorBody();
                    String errorMessage = error.getDefaultMessage();
                    String fieldName = Arrays.stream(((FieldError) error).getField().split("\\.")).reduce((first, second) -> second).orElse("Field");
                    if (StringUtils.isNumeric(errorMessage)) {
                        int errorCode = Integer.parseInt(errorMessage);
                        errorBody.setErrorCode(errorCode);
                        errorMessage = ExceptionMessageLocalizer.toLocale(String.valueOf(errorCode));
                        errorBody.setErrorMessage(errorMessage);
                    } else {
                        errorBody.setErrorMessage(String.format("%s - %s", fieldName, errorMessage));
                        errorBody.setErrorCode(DaoExceptionErrorCode.VALIDATION_ERROR);
                    }
                    errors.add(errorBody);
                }
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    /**
     * Handles exception from <code>org.springframework.validation.annotation.Validated</code> annotation
     * */
    @ExceptionHandler
    public ResponseEntity<List<ErrorBody>> handleConstraintViolationException(ConstraintViolationException ex) {
        List<ErrorBody> errors = new LinkedList<>();
        ex.getConstraintViolations()
                .forEach(e -> {
                    String className = e.getRootBeanClass().getSimpleName();
                    String property = e.getPropertyPath().toString();
                    String message = e.getMessage();
                    String invalidValue = e.getInvalidValue().toString();
                    String fullMessage = String.format("%s.%s value '%s' %s", className, property, invalidValue, message);
                    ErrorBody exceptionDto = new ErrorBody(
                            fullMessage, DaoExceptionErrorCode.VALIDATION_ERROR
                    );
                    errors.add(exceptionDto);
                });
        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorBody> handleDuplicateKeyException(DuplicateKeyException ex) {
        String errorMessage = ExceptionMessageLocalizer.toLocale("40010");
        ErrorBody errorBody = new ErrorBody(errorMessage, 40010);
        return ResponseEntity.status(HttpStatus.valueOf(errorBody.getErrorCode() / 100)).body(errorBody);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorBody> handleIncorrectUpdateValueException(IncorrectUpdateValueException ex) {
        String errorMessage = ExceptionMessageLocalizer.toLocale(String.valueOf(ex.getErrorCode()));
        ErrorBody errorBody = new ErrorBody(errorMessage, ex.getErrorCode());
        return ResponseEntity.status(HttpStatus.valueOf(errorBody.getErrorCode() / 100)).body(errorBody);
    }
}
