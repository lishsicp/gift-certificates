package com.epam.esm.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class PersistentException extends RuntimeException {

    private final int errorCode;
    private final Object parameter;
}
