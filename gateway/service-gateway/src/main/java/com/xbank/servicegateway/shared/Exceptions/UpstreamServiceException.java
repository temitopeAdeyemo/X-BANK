package com.xbank.servicegateway.shared.Exceptions;

public class UpstreamServiceException extends RuntimeException {
    public UpstreamServiceException(String message){
        super(message);
    }
    public UpstreamServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
