package com.xbank.servicegateway.modules.user.service.Impl;

import com.xbank.servicegateway.modules.user.service.UserService;
import com.xbank.servicegateway.shared.service.UserClient;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import proto.user.proto.SignupRequest;
import proto.user.proto.SignupResponse;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserClient userClient;

    @Override
    public SignupResponse registerUser() {
        
        return this.userClient.registerUser(SignupRequest.newBuilder().build());
    }
}
