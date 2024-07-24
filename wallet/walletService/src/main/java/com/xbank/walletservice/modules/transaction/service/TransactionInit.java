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

    }

    @Override
    public void fundWallet(FundWalletRequest request, StreamObserver<StatusResponse> responseObserver) {

    }
}
