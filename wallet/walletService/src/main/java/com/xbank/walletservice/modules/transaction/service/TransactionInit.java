package com.xbank.walletservice.modules.transaction.service;

import io.grpc.stub.StreamObserver;
import proto.service.proto.TransactionInitServiceGrpc;
import proto.transaction.proto.FundTransferRequest;
import proto.transaction.proto.FundTransferResponse;
import proto.transaction.proto.FundWalletRequest;
import proto.transaction.proto.StatusResponse;

public class TransactionInit extends TransactionInitServiceGrpc.TransactionInitServiceImplBase {
    @Override
    public void fundTransfer(FundTransferRequest request, StreamObserver<FundTransferResponse> responseObserver) {

    }

    @Override
    public void fundWallet(FundWalletRequest request, StreamObserver<StatusResponse> responseObserver) {

    }
}
