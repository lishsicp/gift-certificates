package com.epam.esm.exception;

public class ResourceNotFoundException extends RuntimeException {

    private final transient ErrorBody errorBody;

    public ResourceNotFoundException(ErrorBody errorBody) {
        super();
        this.errorBody = errorBody;
    }

    public ResourceNotFoundException(Throwable cause, ErrorBody errorBody) {
        super(cause);
        this.errorBody = errorBody;
    }

    public ResourceNotFoundException(String message, Throwable cause, ErrorBody errorBody) {
        super(message, cause);
        this.errorBody = errorBody;
    }

    public ErrorBody getErrorBody() {
        return errorBody;
    }
}
