package com.resustainability.fincorehub.exception;

import org.springframework.http.HttpStatus;

public class UnauthorizedException extends BaseException {
    public static final String DEFAULT_MESSAGE = "Unauthorized access. You do not have the necessary permissions to access this resource.";

    public UnauthorizedException() {
        super(HttpStatus.FORBIDDEN);
    }
    public UnauthorizedException(boolean defaultResponse) {
        super(HttpStatus.FORBIDDEN, defaultResponse ? DEFAULT_MESSAGE : null);
    }
    public UnauthorizedException(String errorMessage) {
        super(HttpStatus.FORBIDDEN, errorMessage);
    }
}
