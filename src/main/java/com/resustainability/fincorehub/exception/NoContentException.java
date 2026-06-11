package com.resustainability.fincorehub.exception;

import java.util.List;

import org.springframework.http.HttpStatus;


public class NoContentException extends BaseException {
    public NoContentException() {
        super(HttpStatus.NO_CONTENT, List.of());
    }
    public NoContentException(String errorMessage) {
        super(HttpStatus.NO_CONTENT, errorMessage);
    }
}
