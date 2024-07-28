package com.xbank.walletservice.shared.utils;

import io.grpc.stub.StreamObserver;

public class ResponseHandler<T>{
    public void sendSync(StreamObserver<T> responseObserver, T responseBuild) {
        responseObserver.onNext(responseBuild);
        responseObserver.onCompleted();
    }
}
