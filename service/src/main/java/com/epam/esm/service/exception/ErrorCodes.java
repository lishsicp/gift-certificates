package com.epam.esm.service.exception;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ErrorCodes {

    public final int TAG_NOT_FOUND = 40401;
    public final int CERTIFICATE_NOT_FOUND = 40402;
    public final int VALIDATION_ERROR = 40012;
    public final int DUPLICATE_TAG = 40010;
    public final int DUPLICATE_CERTIFICATE = 40011;
    public final int SAVE_FAILURE = 50001;
}

