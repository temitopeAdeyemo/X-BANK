package com.xbank.walletservice.modules.transaction.service;

import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;
import proto.service.proto.GetTransactionServiceGrpc;
import proto.transaction.proto.*;

@RequiredArgsConstructor
@GrpcService
public class GetTransaction extends GetTransactionServiceGrpc.GetTransactionServiceImplBase {
    @Override
    public void getSingleTransaction(GetSingleTransactionRequest request, StreamObserver<Transaction> responseObserver) {

    }

    @Override
    public void getBalance(GetBalanceRequest request, StreamObserver<GetBalanceResponse> responseObserver) {

    }

    @Override
    public void getAllTransactions(GetAllTransactionsRequest request, StreamObserver<GetAllTransactionsResponse> responseObserver) {

    }
}
