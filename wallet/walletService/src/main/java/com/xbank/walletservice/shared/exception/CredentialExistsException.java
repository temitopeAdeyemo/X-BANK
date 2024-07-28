package com.xbank.walletservice.shared.exception;

public class CredentialExistsException extends RuntimeException{
    public CredentialExistsException(String message){
        super(message);
    }
}
