package com.xbank.servicegateway.modules.wallet.dto;

import lombok.Data;

@Data
public class InitTransactionResponse {
    public String status;
    public String referenceNumber;
}
