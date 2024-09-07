package com.xbank.servicegateway.modules.wallet.dto;

import com.xbank.servicegateway.shared.annotations.NullOrLength;
import org.hibernate.validator.constraints.UUID;

public class GetWalletRequest {
    @NullOrLength(message = "Account number must be null or has 10 digits if provided",  minLength = 10, maxLength = 10)
    public String accountNumber;
}

