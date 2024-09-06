package com.xbank.servicegateway.modules.wallet.dto;

import com.xbank.servicegateway.modules.user.dto.UserDto;
import lombok.Data;

@Data
public class WalletDto {
        public String id;
        public String accountNumber;
        public String balance;
        public String walletType;
        public UserDto user;
        public String createdAt;
        public String updatedAt;
}
