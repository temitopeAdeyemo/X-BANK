package com.xbank.servicegateway.shared.Exceptions;

public class ApiRequestException extends RuntimeException{
    public ApiRequestException() {
    }

    public ApiRequestException(String message) {
        super(message);
    }

    public ApiRequestException(String message, Throwable cause) {
        super(message, cause);
    }
}
