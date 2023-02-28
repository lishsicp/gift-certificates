package com.epam.esm.service.exception;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ErrorCodes {

    public final int RESOURCE_NOT_FOUND = 40401;
    public final int CERTIFICATE_NOT_FOUND = 40402;
    public final int VALIDATION_ERROR = 40010;
    public final int DUPLICATED_TAG = 40011;
    public final int DUPLICATED_CERTIFICATE = 40012;
}

