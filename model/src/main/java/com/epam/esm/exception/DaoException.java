package com.epam.esm.exception;

public class DaoException extends Exception {

    private final int errorCode;

    public DaoException(int errorCode) {
        super();
        this.errorCode = errorCode;
    }

    public int getErrorCode() {
        return errorCode;
    }
}
