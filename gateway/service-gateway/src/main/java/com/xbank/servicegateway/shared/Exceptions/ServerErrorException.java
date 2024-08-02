package com.xbank.servicegateway.shared.Exceptions;

public class ServerErrorException extends RuntimeException{
    public ServerErrorException (String message){
        super(message);
    }
}
