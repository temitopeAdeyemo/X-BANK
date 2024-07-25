package com.xbank.walletservice.modules.transaction.service;

import com.xbank.walletservice.modules.transaction.repository.TransactionRepository;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;
import proto.service.proto.TransactionInitServiceGrpc;
import proto.transaction.proto.FundTransferRequest;
import proto.transaction.proto.FundTransferResponse;
import proto.transaction.proto.FundWalletRequest;
import proto.transaction.proto.StatusResponse;

@RequiredArgsConstructor
@GrpcService
public class TransactionInit extends TransactionInitServiceGrpc.TransactionInitServiceImplBase {
    private final TransactionRepository transactionRepository;
    @Override
    public void fundTransfer(FundTransferRequest request, StreamObserver<FundTransferResponse> responseObserver) {
        // track session with user id for fund transfer. If user id session is still available during fund transfer, decline transaction
        // implement status
    }

    @Override
    public void fundWallet(FundWalletRequest request, StreamObserver<StatusResponse> responseObserver) {
        // track session with user id for fund Wallet. If user id session is still available during fund Wallet, decline transaction
        // implement status
    }
}
