package com.xbank.walletservice.modules.walletManagement.service;

import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import proto.service.proto.WalletServiceGrpc;
import proto.wallet.proto.SayHelloResponse;

@GrpcService
public class SayHello extends WalletServiceGrpc.WalletServiceImplBase {
    @Override
    public void sayHelloService(proto.wallet.proto.SayHello request, StreamObserver<SayHelloResponse> responseObserver) {
        responseObserver.onNext(SayHelloResponse.newBuilder().setMessage("Hello World").build());
        responseObserver.onCompleted();
    }
}
