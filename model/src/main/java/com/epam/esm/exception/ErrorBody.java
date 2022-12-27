package com.epam.esm.exception;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import java.io.Serializable;

@Builder
@Getter
public class ErrorBody implements Serializable {

    private static final String ID_ARG = "(id = %d)";

    private String errorMessage;
    private int errorCode;
    @JsonIgnore
    private long resourceId;

    public ErrorBody(String errorMessage, int errorCode, long resourceId) {
        this.errorMessage = String.format(String.format(errorMessage, ID_ARG), resourceId);
        this.errorCode = errorCode;
    }
}
