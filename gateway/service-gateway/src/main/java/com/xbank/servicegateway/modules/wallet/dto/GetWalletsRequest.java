package com.xbank.servicegateway.modules.wallet.dto;

import lombok.Data;

@Data
public class GetWalletsRequest {
    public String WalletType;
    public String UserId;
}
