package com.resustainability.fincorehub.commons;

public record APIResponse<T> (
        T data,
        String message,
        String error
) {
    public APIResponse(T data) {
        this(data, null, null);
    }

    public APIResponse(String message) {
        this(null, message, null);
    }

    public APIResponse(String message, T data) {
        this(data, message, null);
    }

    public APIResponse(T data, String error) {
        this(data, null, error);
    }
}
