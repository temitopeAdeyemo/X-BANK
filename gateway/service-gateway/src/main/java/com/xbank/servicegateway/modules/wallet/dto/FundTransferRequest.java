package com.xbank.servicegateway.modules.wallet.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.validator.constraints.UUID;

import javax.annotation.Nonnegative;

@Data
public class FundTransferRequest {
    @NotBlank
    @NotNull
    @Size(min = 10, max = 10, message = "Account number must be 10 digits")
    public String receiverAccountNumber;

    @NotBlank
    @NotNull
    public String sourceCode;

    @NotBlank
    @NotNull
    @Nonnegative
    public Long amount;
}
