package com.xbank.servicegateway.modules.user.service;

import proto.user.proto.SignupResponse;

public interface UserService {
    SignupResponse registerUser();
}
