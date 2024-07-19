package com.xbank.walletservice.modules.walletManagement.service;

import io.grpc.stub.StreamObserver;
import proto.service.proto.WalletServiceGrpc;
import proto.wallet.proto.SayHelloResponse;

public class SayHello extends WalletServiceGrpc.WalletServiceImplBase {
    @Override
    public void sayHelloService(proto.wallet.proto.SayHello request, StreamObserver<SayHelloResponse> responseObserver) {
        
    }
}
