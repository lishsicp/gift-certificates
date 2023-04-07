package com.epam.esm.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ErrorBody {

    private final String errorMessage;
    private final int errorCode;
}
