package com.xbankuser.userservice.shared.exception;

public class ServerErrorException extends RuntimeException{
    public ServerErrorException (String message){
        super(message);
    }
}
