package com.xbank.servicegateway.shared.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import proto.service.proto.*;
import proto.transaction.proto.*;
import proto.user.proto.Empty;
import proto.wallet.proto.*;

@RequiredArgsConstructor
@Service
public class WalletClient {
    WalletServiceGrpc.WalletServiceBlockingStub synchronousWalletService;
    GetWalletServiceGrpc.GetWalletServiceBlockingStub synchronousGetWalletService;
    DeleteWalletServiceGrpc.DeleteWalletServiceBlockingStub synchronousDeleteWalletService;
    TransactionInitServiceGrpc.TransactionInitServiceBlockingStub synchronousTransactionInitService;
    GetTransactionServiceGrpc.GetTransactionServiceBlockingStub synchronousGetTransactionService;

    public CreateWalletResponse createWallet(CreateWalletRequest request){
        return synchronousWalletService.createWallet(request);
    }

    public Empty deleteWallet(DeleteWalletRequest request){
        return synchronousDeleteWalletService.deleteWallet(request);
    }

    public Wallet getWallet(GetSingleWalletRequest request){
        return synchronousGetWalletService.getSingleWallet(request);
    }

    public GetWalletsResponse getWallets(GetWalletsRequest request){
        return synchronousGetWalletService.getWallets(request);
    }

    public GetBalanceResponse getBalance(GetBalanceRequest request){
        return synchronousGetWalletService.getBalance(request);
    }

    public FundDepositResponse fundWallet(FundWalletRequest request){
        return synchronousTransactionInitService.fundWallet(request);
    }

    public FundDepositResponse fundTransfer(FundTransferRequest request){
        return synchronousTransactionInitService.fundTransfer(request);
    }

    public Transaction getSingleTransaction(GetSingleTransactionRequest request){
        return synchronousGetTransactionService.getSingleTransaction(request);
    }

    public GetAllTransactionsResponse getAllTransactions(GetAllTransactionsRequest request){
        return synchronousGetTransactionService.getAllTransactions(request);
    }
}
