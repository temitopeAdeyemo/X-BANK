package com.xbankuser.userservice.modules.auth.service;

import com.xbankuser.userservice.modules.auth.entiy.User;
import com.xbankuser.userservice.modules.auth.repository.UserRepository;
import com.xbankuser.userservice.shared.exception.InvalidOtpException;
import com.xbankuser.userservice.shared.exception.UserNotFoundException;
import com.xbankuser.userservice.shared.exception.UserVerifiedException;
import com.xbankuser.userservice.shared.service.cache.RedisService;
import com.xbankuser.userservice.shared.service.emailClient.SibClient;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;
import proto.service.proto.VerifyEmailServiceGrpc;
import proto.user.proto.Status;
import proto.verifyEmail.proto.RequestOtpRequest;
import proto.verifyEmail.proto.RequestOtpResponse;
import proto.verifyEmail.proto.VerifyOtpRequest;
import proto.verifyEmail.proto.VerifyOtpResponse;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@GrpcService
@RequiredArgsConstructor
public class VerifyEmail extends VerifyEmailServiceGrpc.VerifyEmailServiceImplBase {
    private final UserRepository userRepository;
    private final SibClient sibClient;
    private final RedisService redisService;
    public String cacheIdPath = "VERIFICATION/EMAIL/";

    @Override
    public void requestOtp(RequestOtpRequest request, StreamObserver<RequestOtpResponse> responseObserver) {
        var userEmailExists = this.userRepository.findByEmail(request.getEmail()).orElseThrow(()->new UserNotFoundException("User not found."));

        if(userEmailExists.getEmailVerified()) throw new UserVerifiedException("User already verified.");

        String otp = String.valueOf(Math.ceil(Math.random()*100000));

        this.redisService.set(cacheIdPath+request.getEmail(), otp, 900, TimeUnit.SECONDS);

        sibClient.sendEmail(request.getEmail(), "EMAIL VERIFICATION", "Verify your email with the otp: "+ otp);

        responseObserver.onNext(RequestOtpResponse.newBuilder().setStatus(Status.SUCCESSFUL).setOtp(otp).build());
        responseObserver.onCompleted();
    }

    @Override
    public void verifyOtp(VerifyOtpRequest request, StreamObserver<VerifyOtpResponse> responseObserver) {
        var userEmailExists = this.userRepository.findByEmail(request.getEmail()).orElseThrow(()->new UserNotFoundException("User not found."));

        if(userEmailExists.getEmailVerified()) throw new UserVerifiedException("User already verified.");

        String otp = this.redisService.get(cacheIdPath+request.getEmail());

        if(!otp.equals(request.getOtp())) throw new InvalidOtpException("Invalid OTP");

        responseObserver.onNext(VerifyOtpResponse.newBuilder().setStatus(Status.SUCCESSFUL).build());
        responseObserver.onCompleted();
    }
}
