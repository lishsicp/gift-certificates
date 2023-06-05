package com.epam.esm.exception;

import lombok.Builder;

@Builder
public record ExceptionResponseBody(int errorCode, String errorMessage) {

}
