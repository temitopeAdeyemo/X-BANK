package com.xbankuser.userservice.modules.auth.service;

import com.xbankuser.userservice.modules.auth.entiy.Role;
import com.xbankuser.userservice.modules.auth.repository.UserRepository;
import com.xbankuser.userservice.shared.utils.ContextKeys;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;
import proto.service.proto.UpdateUserServiceGrpc;
import proto.updateUser.proto.UpdateUserRequest;
import proto.updateUser.proto.UpdateUserResponse;
import proto.user.proto.Status;

@RequiredArgsConstructor
@GrpcService
public class UpdateUser extends UpdateUserServiceGrpc.UpdateUserServiceImplBase {
    private final UserRepository userRepository;

    @Override
    public void updateUser(UpdateUserRequest request, StreamObserver<UpdateUserResponse> responseObserver) {
        var user = ContextKeys.user.get();

        var data = request.getData();

        if(!data.getLastName().isEmpty()) user.setLastName(data.getLastName());
        if(!data.getFirstName().isEmpty()) user.setFirstName(data.getFirstName());
        if(!data.getEmail().isEmpty()) user.setEmail(data.getEmail());
        if(!data.getPhoneNumber().isEmpty()) user.setPhoneNumber(data.getPhoneNumber());
        if(!data.getRole().isEmpty()) user.setRole(Role.valueOf(data.getRole()));

        this.userRepository.save(user);

        responseObserver.onNext(UpdateUserResponse.newBuilder().setStatus(Status.SUCCESSFUL).build());
        responseObserver.onCompleted();
    }
}
