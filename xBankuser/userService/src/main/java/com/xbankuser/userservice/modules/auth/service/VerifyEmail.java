package com.xbankuser.userservice.modules.auth.service;

import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;
import proto.service.proto.VerifyEmailServiceGrpc;
import proto.verifyEmail.proto.RequestOtpRequest;
import proto.verifyEmail.proto.RequestOtpResponse;
import proto.verifyEmail.proto.VerifyOtpRequest;
import proto.verifyEmail.proto.VerifyOtpResponse;

@GrpcService
@RequiredArgsConstructor
public class VerifyEmail extends VerifyEmailServiceGrpc.VerifyEmailServiceImplBase {
    @Override
    public void requestOtp(RequestOtpRequest request, StreamObserver<RequestOtpResponse> responseObserver) {

    }

    @Override
    public void verifyOtp(VerifyOtpRequest request, StreamObserver<VerifyOtpResponse> responseObserver) {

    }
}
