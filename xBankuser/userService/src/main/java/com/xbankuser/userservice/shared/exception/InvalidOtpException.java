package com.xbankuser.userservice.shared.exception;

public class InvalidOtpException extends RuntimeException{
    public InvalidOtpException(String message){
        super(message);
    }
}
