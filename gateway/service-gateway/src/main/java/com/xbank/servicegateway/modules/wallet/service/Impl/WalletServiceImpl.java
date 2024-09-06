package com.xbank.servicegateway.modules.wallet.service.Impl;

import com.xbank.servicegateway.modules.user.dto.StatusResponse;
import com.xbank.servicegateway.modules.wallet.dto.CreateWalletResponse;
import com.xbank.servicegateway.modules.wallet.service.WalletService;
import com.xbank.servicegateway.shared.service.WalletClient;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import proto.transaction.proto.*;
import proto.user.proto.Empty;
import proto.wallet.proto.*;

@AllArgsConstructor
@Service
public class WalletServiceImpl implements WalletService {
    private final WalletClient walletClient;
    @Override
    public CreateWalletResponse createWallet(String userId) {
        var response = this.walletClient.createWallet(CreateWalletRequest.newBuilder().setUserId(userId).build());

        var walletData =  new CreateWalletResponse();
        walletData.setAccountNumber(response.getAccountNumber());

        return walletData;
    }

    public StatusResponse deleteWallet(String userId){
        this.walletClient.deleteWallet(DeleteWalletRequest.newBuilder().setId(userId).build());

        var status = new StatusResponse();

        status.setStatus("SUCCESS");

        return status;
    }

    public void getWallet(){}

    public void getWallets(){}

    public void getBalance(){}

    public void fundWallet(){}

    public void fundTransfer(){}

    public void getSingleTransaction(){}

    public void getAllTransactions(){}
}