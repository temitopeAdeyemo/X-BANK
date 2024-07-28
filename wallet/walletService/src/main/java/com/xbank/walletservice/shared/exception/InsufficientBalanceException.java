package com.xbank.walletservice.shared.exception;

import javax.annotation.Nullable;

public class InsufficientBalanceException extends RuntimeException{
    public InsufficientBalanceException(String message){
        super(message);
    }
}
