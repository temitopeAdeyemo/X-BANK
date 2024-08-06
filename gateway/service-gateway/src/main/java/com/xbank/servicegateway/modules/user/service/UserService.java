package com.xbank.servicegateway.modules.user.service;

import com.xbank.servicegateway.modules.user.dto.LoginDto;
import com.xbank.servicegateway.modules.user.dto.UserRegistrationDto;
import proto.user.proto.LoginResponse;
import proto.user.proto.SignupResponse;

public interface UserService {
    SignupResponse registerUser(UserRegistrationDto data);
    LoginResponse loginUser(LoginDto data);
}
