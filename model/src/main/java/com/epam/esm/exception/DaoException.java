package com.epam.esm.exception;

public class DaoException extends Exception {

    private final int errorCode;
    private Object parameter;

    public DaoException(int errorCode, Object parameter) {
        super();
        this.errorCode = errorCode;
        this.parameter = parameter;
    }

    public DaoException(int errorCode) {
        super();
        this.errorCode = errorCode;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public Object getParameter() {
        return parameter;
    }
}
