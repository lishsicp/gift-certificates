package com.epam.esm.exception;

public class ErrorMessage {
    private ErrorMessage() {}

    public static final String RESOURCE_NOT_FOUND = "Requested resource not found (id = %d)";

    public static final String DUPLICATED_TAG = "Duplicate tag name. Tag with name '%s' already exist";

    public static final String INVALID_FILTER_PARAM = "Invalid filter parameter.";
}
