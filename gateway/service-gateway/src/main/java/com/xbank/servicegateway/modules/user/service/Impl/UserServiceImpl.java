package com.xbank.servicegateway.modules.user.service.Impl;

import com.xbank.servicegateway.modules.user.dto.LoginDto;
import com.xbank.servicegateway.modules.user.dto.UserRegistrationDto;
import com.xbank.servicegateway.modules.user.service.UserService;
//import com.xbank.servicegateway.shared.service.UserClient;
//import jakarta.validation.Valid;
import com.xbank.servicegateway.shared.service.UserClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
//import org.springframework.web.bind.annotation.RequestBody;
//import proto.user.proto.SignupRequest;
import proto.user.proto.LoginRequest;
import proto.user.proto.LoginResponse;
import proto.user.proto.SignupRequest;
import proto.user.proto.SignupResponse;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserClient userClient;

    @Override
    public SignupResponse registerUser(UserRegistrationDto data) {
        var requestData = SignupRequest.newBuilder()
                .setEmail(data.getEmail())
                .setPhoneNumber(data.getPhone_number())
                .setFirstName(data.getFirst_name())
                .setLastName(data.getLast_name())
                .setPassword(data.getPassword())
                .build();

        var response =  this.userClient.registerUser(requestData);

        return response.toBuilder().setStatus(response.getStatus()).build();
    }

    @Override
    public LoginResponse loginUser(LoginDto data) {
        var requestData = LoginRequest.newBuilder().setEmail(data.getEmail()).setPassword(data.getPassword()).build();
        return this.userClient.loginUser(requestData);
    }
}
