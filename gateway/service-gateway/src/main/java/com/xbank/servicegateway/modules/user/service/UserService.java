package com.xbank.servicegateway.modules.user.service;

import com.xbank.servicegateway.modules.user.dto.*;
import proto.getUser.proto.GetAllUsersResponse;
import proto.getUser.proto.User;
import proto.updateUser.proto.UpdateUserResponse;
import proto.user.proto.LoginResponse;
import proto.user.proto.SignupResponse;
import proto.verifyEmail.proto.RequestOtpResponse;
import proto.verifyEmail.proto.VerifyOtpResponse;

import java.util.List;

public interface UserService {
    UserRegistrationDataMapper registerUser(UserRegistrationDto data);
    LoginResponse loginUser(LoginDto data);

    UserDto getUser(String user_id);

    StatusResponse updateUser(UpdateUserDto data);

    StatusResponse requestVerificationEmail(String email);

    StatusResponse validateVerificationOtp(String email, String otp);

    List<UserDto> getAllUsers(GetUsersDto filter, int page, int size);
}
