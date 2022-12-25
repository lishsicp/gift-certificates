package com.epam.esm.exception;

public enum DBErrorCodes {

    NOT_FOUND_BY_ID("40401");

    private final String errorCode;

    DBErrorCodes(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }
}
