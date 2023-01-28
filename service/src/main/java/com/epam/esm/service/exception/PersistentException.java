package com.epam.esm.service.exception;

public class PersistentException extends RuntimeException {

    private int errorCode;
    private Object parameter;

    public PersistentException(int errorCode, Object parameter) {
        super();
        this.errorCode = errorCode;
        this.parameter = parameter;
    }

    public PersistentException(int errorCode) {
        super();
        this.errorCode = errorCode;
    }

    public PersistentException() {
        super();
    }

    public int getErrorCode() {
        return errorCode;
    }

    public Object getParameter() {
        return parameter;
    }
}
