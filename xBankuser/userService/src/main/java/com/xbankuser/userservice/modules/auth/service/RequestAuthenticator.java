package com.xbankuser.userservice.modules.auth.service;

import com.xbankuser.userservice.shared.mapper.UserDataMapper;
import com.xbankuser.userservice.shared.utils.ContextKeys;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;
import proto.getUser.proto.User;
import proto.service.proto.RequestAuthenticatorGrpc;
import proto.user.proto.Empty;

@RequiredArgsConstructor
@GrpcService
public class RequestAuthenticator extends RequestAuthenticatorGrpc.RequestAuthenticatorImplBase {
    @Override
    public void authenticateUser(Empty request, StreamObserver<User> responseObserver) {
        System.out.println("|||||||||||||||||||||||||||");
        var user = ContextKeys.user.get();

        User userBuild = UserDataMapper.mapUserToProtobuf(user);

        responseObserver.onNext(userBuild);
        responseObserver.onCompleted();
    }
}
