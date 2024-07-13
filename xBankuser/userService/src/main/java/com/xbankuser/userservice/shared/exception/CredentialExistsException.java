package com.xbankuser.userservice.shared.exception;

public class CredentialExistsException extends RuntimeException{
    public CredentialExistsException(String message){
        super(message);
    }
}
