package com.resustainability.fincorehub.exception;

import org.springframework.http.HttpStatus;

import com.resustainability.fincorehub.commons.APIResponse;
import com.resustainability.fincorehub.commons.StringUtils;



public class BaseException extends RuntimeException {
    private final HttpStatus status;
    private final Throwable exception;
    private final APIResponse<Object> response;
    private final boolean emptyBody;

    public BaseException(HttpStatus status) {
        super();
        this.status = status;
        this.exception = null;
        this.response = null;
        this.emptyBody = true;
    }

    public BaseException(HttpStatus status, String errorMessage) {
        super(errorMessage);
        this.status = status;
        this.exception = null;
        this.response = new APIResponse<>(null, null, errorMessage);
        this.emptyBody = StringUtils.isBlank(errorMessage);
    }

    public <T> BaseException(HttpStatus status, T data) {
        super();
        this.status = status;
        this.exception = null;
        this.response = new APIResponse<>(data);
        this.emptyBody = null == data || data instanceof String str && StringUtils.isBlank(str);
    }

    public BaseException(HttpStatus status, Throwable exception) {
        super(exception.getMessage());
        this.status = status;
        this.exception = exception;
        this.response = new APIResponse<>(null, null, exception.getMessage());
        this.emptyBody = StringUtils.isBlank(exception.getMessage());
    }

    public BaseException(HttpStatus status, Throwable exception, String errorMessage) {
        super(errorMessage);
        this.status = status;
        this.exception = exception;
        this.response = new APIResponse<>(null, null, errorMessage);
        this.emptyBody = StringUtils.isBlank(errorMessage);
    }

    public HttpStatus getStatus() {
        return status;
    }

    public Throwable getException() {
        return exception;
    }

    public APIResponse<Object> getResponse() {
        return response;
    }

    public boolean isEmptyBody() {
        return emptyBody;
    }
}


