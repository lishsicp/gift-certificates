package com.epam.esm.exception;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
public class ErrorBody implements Serializable {

    private String errorMessage;
    private int errorCode;

    public ErrorBody(String errorMessage, int errorCode) {
        this.errorMessage = errorMessage;
        this.errorCode = errorCode;
    }

    public ErrorBody() {

    }
}
