package com.epam.esm.exception;

public class ResourceNotFoundException extends AbstractErrorBodyException {

    public ResourceNotFoundException(ErrorBody errorBody) {
        super(errorBody);
    }

}
