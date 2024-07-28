package com.xbank.walletservice.shared.exception;

public class TransactionDeclinedException extends RuntimeException{
    public TransactionDeclinedException(String message){
        super(message);
    }
}
