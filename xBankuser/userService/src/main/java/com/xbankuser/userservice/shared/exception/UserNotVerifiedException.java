package com.xbankuser.userservice.shared.exception;

public class UserNotVerifiedException extends RuntimeException{
    public UserNotVerifiedException(String message){
        super(message);
    }
}
