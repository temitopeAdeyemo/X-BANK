package com.xbank.walletservice.shared.mapper;

import com.xbank.walletservice.modules.wallet.entity.Wallet;

public class WalletDataMapper {
    public static proto.wallet.proto.Wallet mapWalletToProtobuf(Wallet wallet){

        return proto.wallet.proto.Wallet.newBuilder().setAccountNumber(wallet.getAccountNumber())
                .setBalance(String.valueOf(wallet.getBalance()))
                .setWalletType(wallet.getWalletType())
                .setCreatedAt(String.valueOf(wallet.getCreatedAt()))
                .setUpdatedAt(String.valueOf(wallet.getCreatedAt()))
                .setId(String.valueOf(wallet.getId()))
                .build();
    }
}
