package com.epam.esm.service.exception;

public class IncorrectUpdateValueException extends Exception {
    final int errorCode;

    public IncorrectUpdateValueException(int errorCode) {
        this.errorCode = errorCode;
    }

    public int getErrorCode() {
        return errorCode;
    }
}