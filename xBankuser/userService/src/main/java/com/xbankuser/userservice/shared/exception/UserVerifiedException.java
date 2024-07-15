package com.xbankuser.userservice.shared.exception;

public class UserVerifiedException extends RuntimeException{
    public UserVerifiedException(String message){
        super(message);
    }
}
