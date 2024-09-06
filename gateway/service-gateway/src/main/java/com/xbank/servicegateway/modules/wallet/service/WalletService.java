package com.xbank.servicegateway.modules.wallet.service;

import com.xbank.servicegateway.modules.user.dto.StatusResponse;
import com.xbank.servicegateway.modules.wallet.dto.CreateWalletResponse;
import com.xbank.servicegateway.modules.wallet.dto.GetWalletRequest;
import com.xbank.servicegateway.modules.wallet.dto.WalletDto;

import java.util.List;

public interface WalletService {
    CreateWalletResponse createWallet(String userId);

    StatusResponse deleteWallet(String userId);

    WalletDto getWallet(GetWalletRequest payload);

    List<WalletDto> getWallets(com.xbank.servicegateway.modules.wallet.dto.GetWalletsRequest payload, int page, int size);

    void getBalance();

    void fundWallet();

    void fundTransfer();

    void getSingleTransaction();

    void getAllTransactions();
}
