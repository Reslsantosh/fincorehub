package com.resustainability.fincorehub.exception;

import org.springframework.http.HttpStatus;

public class InvalidDataException extends BaseException {
    public InvalidDataException(String errorMessage) {
        super(HttpStatus.BAD_REQUEST, errorMessage);
    }
    public InvalidDataException(Exception exception) {
        super(HttpStatus.BAD_REQUEST, exception);
    }
    public InvalidDataException(Exception exception, String errorMessage) {
        super(HttpStatus.BAD_REQUEST, exception, errorMessage);
    }
}
