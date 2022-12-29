package com.epam.esm.exception;

import lombok.*;

import java.io.Serializable;

@Getter
public class ErrorBody implements Serializable {

    private static final String ID_ARG = "(id = %d)";

    private final String errorMessage;
    private final int errorCode;

    public ErrorBody(String errorMessage, int errorCode, long resourceId) {
        this.errorMessage = String.format(String.format(errorMessage, ID_ARG), resourceId);
        this.errorCode = errorCode;
    }

    public ErrorBody(String errorMessage, int errorCode, String value) {
        this.errorMessage = String.format(errorMessage, value);
        this.errorCode = errorCode;
    }
}
