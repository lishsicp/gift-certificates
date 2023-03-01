package com.epam.esm.service.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class PersistenceException extends RuntimeException {

    private final int errorCode;
    private final transient Object parameter;
}
