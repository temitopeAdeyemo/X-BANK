package com.xbank.servicegateway.shared.Exceptions;

public class SystemBusyException extends RuntimeException {
    public SystemBusyException(String message) {
        super(message);
    }
}