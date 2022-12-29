package com.epam.esm.exception;

public class DuplicatedKeyException extends AbstractErrorBodyException {
    public DuplicatedKeyException(ErrorBody errorBody) {
        super(errorBody);
    }
}
