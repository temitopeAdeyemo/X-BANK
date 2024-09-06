package com.xbank.servicegateway.modules.wallet.service;

import com.xbank.servicegateway.modules.user.dto.StatusResponse;
import com.xbank.servicegateway.modules.wallet.dto.CreateWalletResponse;

  public interface WalletService {
    CreateWalletResponse createWallet(String userId);

    StatusResponse deleteWallet(String userId);

    void getWallet();

    void getWallets();

    void getBalance();

    void fundWallet();

    void fundTransfer();

    void getSingleTransaction();

    void getAllTransactions();
}
