package com.xbankuser.userservice.modules.auth.service;

import com.xbankuser.userservice.modules.auth.entiy.Role;
import com.xbankuser.userservice.modules.auth.entiy.User;
import com.xbankuser.userservice.modules.auth.repository.UserRepository;
import com.xbankuser.userservice.shared.exception.CredentialExistsException;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.security.crypto.password.PasswordEncoder;
import proto.service.proto.RegisterServiceGrpc;
import proto.user.proto.SignupRequest;
import proto.user.proto.SignupResponse;
import proto.user.proto.Status;

import java.util.Optional;

@GrpcService
@RequiredArgsConstructor
public class Register extends RegisterServiceGrpc.RegisterServiceImplBase {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    @Override
    public void registerUser(SignupRequest request, StreamObserver<SignupResponse> responseObserver) {
        Optional<User> userEmailExists = this.userRepository.findByEmail(request.getEmail());

        if(userEmailExists.isPresent()) throw new CredentialExistsException("Email Taken");

        Optional<User> userPhoneExists = this.userRepository.findByPhoneNumber(request.getPhoneNumber());

        if(userPhoneExists.isPresent()) throw new CredentialExistsException("Phone number Taken");

        String hashedPassword = passwordEncoder.encode(request.getPassword());

        User userData = new User(null, request.getEmail(), request.getPhoneNumber(), hashedPassword, request.getLastName(), request.getFirstName(), false, Role.USER, null, null);

        var user = this.userRepository.save(userData);

        responseObserver.onNext(SignupResponse.newBuilder().setStatus(Status.SUCCESSFUL).setId(String.valueOf(user.getId())).build());
        responseObserver.onCompleted();
    }
}