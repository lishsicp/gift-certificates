package com.epam.esm.exception;

public class ResourceNotFoundException extends AbstractErrorBodyException {

    public ResourceNotFoundException(ErrorBody errorBody) {
        super(errorBody);
    }

    public ResourceNotFoundException(Throwable cause, ErrorBody errorBody) {
        super(cause, errorBody);
    }

    public ResourceNotFoundException(String message, Throwable cause, ErrorBody errorBody) {
        super(message, cause, errorBody);
    }
}
