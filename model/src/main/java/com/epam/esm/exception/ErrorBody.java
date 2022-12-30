package com.epam.esm.exception;

import lombok.*;

import java.io.Serializable;

@Getter
public class ErrorBody implements Serializable {

    private final String errorMessage;
    private final int errorCode;

    public ErrorBody(String errorMessage, int errorCode) {
        this.errorMessage = errorMessage;
        this.errorCode = errorCode;
    }
}
