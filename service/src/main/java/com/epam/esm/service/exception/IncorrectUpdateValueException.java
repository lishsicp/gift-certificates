package com.epam.esm.service.exception;

public class IncorrectUpdateValueException extends Exception {
    int errorCode;

    public IncorrectUpdateValueException(int errorCode) {
        this.errorCode = errorCode;
    }
}
