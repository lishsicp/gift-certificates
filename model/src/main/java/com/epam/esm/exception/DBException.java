package com.epam.esm.exception;

public class DBException extends RuntimeException {

    public DBException() {
        super();
    }

    public DBException(Throwable cause) {
        super(cause);
    }

    public DBException(DBErrorCodes errorCode) {
        super(errorCode.getErrorCode());
    }
}
