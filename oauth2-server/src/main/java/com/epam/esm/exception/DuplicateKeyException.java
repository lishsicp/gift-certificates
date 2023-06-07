package com.epam.esm.exception;

public class DuplicateKeyException extends RuntimeException {

    public DuplicateKeyException(String s) {
        super(s);
    }
}
