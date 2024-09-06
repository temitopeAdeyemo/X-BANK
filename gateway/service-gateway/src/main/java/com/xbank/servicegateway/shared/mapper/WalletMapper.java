package com.xbank.servicegateway.shared.mapper;

import com.xbank.servicegateway.modules.user.dto.UserDto;
import com.xbank.servicegateway.modules.wallet.dto.WalletDto;
import proto.getUser.proto.User;
import proto.wallet.proto.Wallet;

import java.util.Date;

public class WalletMapper {
    public WalletDto walletBuilder(WalletDto walletBuild, Wallet walletProto) {
        walletBuild.setId(walletProto.getId());
        walletBuild.setAccountNumber(walletProto.getAccountNumber());
        walletBuild.setBalance(walletProto.getBalance());
        walletBuild.setWalletType(walletProto.getWalletType());
        walletBuild.setCreatedAt(walletProto.getCreatedAt());
        walletBuild.setUpdatedAt(walletProto.getUpdatedAt());

        UserDto userDto = new UserDto();
        var userBuild = new UserMapper().userBuilder(userDto, walletProto.getUser());
        walletBuild.setUser(userBuild);
        return walletBuild;
    }
}
