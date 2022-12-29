package com.epam.esm.exception;

public class ErrorMessage {
    private ErrorMessage() {}

    public static final String RESOURCE_NOT_FOUND = "Requested resource not found %s";

    public static final String DUPLICATED_TAG = "Duplicate tag name. Tag with name '%s' already exist";
}
