package com.xbank.servicegateway.modules.wallet.service;

import com.xbank.servicegateway.modules.user.dto.StatusResponse;
import com.xbank.servicegateway.modules.wallet.dto.*;
import java.util.List;

public interface WalletService {
    CreateWalletResponse createWallet(String userId);

    StatusResponse deleteWallet(String userId);

    public WalletDto getWallet(GetWalletRequest payload, String userId );

    List<WalletDto> getWallets(com.xbank.servicegateway.modules.wallet.dto.GetWalletsRequest payload, int page, int size);

    BalanceResponse getBalance(GetWalletRequest payload, String userId);

    InitTransactionResponse fundWallet(FundWalletRequest payload, String userId);

    InitTransactionResponse fundTransfer(FundTransferRequest payload, String userId);

    void getSingleTransaction();

    void getAllTransactions();
}
