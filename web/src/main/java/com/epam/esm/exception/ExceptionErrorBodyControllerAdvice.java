package com.epam.esm.exception;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ExceptionErrorBodyControllerAdvice {
    @ExceptionHandler(AbstractErrorBodyException.class)
    ResponseEntity<ErrorBody> handleAbstractErrorBodyException(AbstractErrorBodyException ex) {
        ErrorBody errorBody = ex.getErrorBody();
        return ResponseEntity.status(HttpStatus.valueOf(errorBody.getErrorCode() / 100)).body(errorBody);
    }
}
