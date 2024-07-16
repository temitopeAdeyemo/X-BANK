package com.xbankuser.userservice.modules.auth.service;

import com.xbankuser.userservice.config.JwtAuthProvider;
import com.xbankuser.userservice.modules.auth.entiy.Role;
import com.xbankuser.userservice.modules.auth.entiy.User;
import com.xbankuser.userservice.modules.auth.repository.UserRepository;
import com.xbankuser.userservice.shared.exception.UserNotFoundException;
import com.xbankuser.userservice.shared.service.Jwt.JwtService;
import com.xbankuser.userservice.shared.utils.ContextKeys;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import proto.service.proto.UpdateUserServiceGrpc;
import proto.updateUser.proto.UpdateUserRequest;
import proto.updateUser.proto.UpdateUserResponse;
import proto.user.proto.Status;
import java.util.UUID;

@RequiredArgsConstructor
public class UpdateUser extends UpdateUserServiceGrpc.UpdateUserServiceImplBase {
    private final UserRepository userRepository;

    @Override
    public void updateUser(UpdateUserRequest request, StreamObserver<UpdateUserResponse> responseObserver) {
        User user = ContextKeys.user.get();
//        String username = this.jwtService.extractUsername(token);

//        User user = this.userRepository.findByEmail(username).orElseThrow(()->new UserNotFoundException("User Not Found"));
        
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
