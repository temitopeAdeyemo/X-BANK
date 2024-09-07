package com.xbank.servicegateway.modules.wallet.dto;

import lombok.Data;
import org.hibernate.validator.constraints.UUID;

import javax.annotation.Nonnegative;

@Data
public class FundWalletRequest {
    @Nonnegative
    public Long amount;
}

