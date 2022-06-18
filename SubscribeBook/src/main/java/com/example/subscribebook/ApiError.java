package com.example.subscribebook;

import org.springframework.http.HttpStatus;

public class ApiError {

    private HttpStatus status;
    private String message;
    private int error;

    public ApiError(HttpStatus status, String message, int errors) {
        super();
        this.status = status;
        this.message = message;
        this.error = errors;
    }
}
