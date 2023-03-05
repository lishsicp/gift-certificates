package com.epam.esm.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class ErrorBody {

    private final String errorMessage;
    private final int errorCode;
}
