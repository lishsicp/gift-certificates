package com.epam.esm.exception;

public abstract class AbstractErrorBodyException extends RuntimeException {

    private final transient ErrorBody errorBody;

    protected AbstractErrorBodyException(ErrorBody errorBody) {
        super();
        this.errorBody = errorBody;
    }

    protected AbstractErrorBodyException(Throwable cause, ErrorBody errorBody) {
        super(cause);
        this.errorBody = errorBody;
    }

    protected AbstractErrorBodyException(String message, Throwable cause, ErrorBody errorBody) {
        super(message, cause);
        this.errorBody = errorBody;
    }

    protected ErrorBody getErrorBody() {
        return errorBody;
    }

}
