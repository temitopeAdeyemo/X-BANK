package com.xbank.servicegateway.modules.user.service.Impl;

import com.xbank.servicegateway.modules.user.dto.*;
import com.xbank.servicegateway.modules.user.service.UserService;
//import com.xbank.servicegateway.shared.service.UserClient;
//import jakarta.validation.Valid;
import com.xbank.servicegateway.shared.service.UserClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
//import org.springframework.web.bind.annotation.RequestBody;
//import proto.user.proto.SignupRequest;
import proto.getUser.proto.GetAllUsersRequest;
import proto.getUser.proto.GetAllUsersResponse;
import proto.getUser.proto.User;
import proto.updateUser.proto.UpdateUserData;
import proto.updateUser.proto.UpdateUserRequest;
import proto.user.proto.LoginRequest;
import proto.user.proto.LoginResponse;
import proto.user.proto.SignupRequest;
import proto.user.proto.SignupResponse;
import proto.verifyEmail.proto.RequestOtpRequest;
import proto.verifyEmail.proto.VerifyOtpRequest;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserClient userClient;

    @Override
    public UserRegistrationDataMapper registerUser(UserRegistrationDto data) {
        var requestData = SignupRequest.newBuilder()
                .setEmail(data.getEmail())
                .setPhoneNumber(data.getPhone_number())
                .setFirstName(data.getFirst_name())
                .setLastName(data.getLast_name())
                .setPassword(data.getPassword())
                .build();

        var response =  this.userClient.registerUser(requestData);

        return new UserRegistrationDataMapper(response.getStatus().toString(), response.getId());
    }

    @Override
    public LoginResponse loginUser(LoginDto data) {
        var requestData = LoginRequest.newBuilder().setEmail(data.getEmail()).setPassword(data.getPassword()).build();
        return this.userClient.loginUser(requestData);
    }

    @Override
    public UserDto getUser(String user_id) {

        var user = this.userClient.getUserByUniqueField(user_id);
        var userBuild = new UserDto();

        userBuilder(userBuild, user);

        return userBuild;
    }

    @Override
    public StatusResponse updateUser(UpdateUserDto updateData) {
        var data = UpdateUserData.newBuilder()
                .setEmail(updateData.getEmail())
                .setLastName(updateData.getFirst_name())
                .setFirstName(updateData.getFirst_name())
                .setPhoneNumber(updateData.getPhone_number())
                .build();

        var dataBuild = UpdateUserRequest.newBuilder().setData(data).build();
        var response = this.userClient.updateUser(dataBuild);
        var statusResponse = new StatusResponse();
        statusResponse.setStatus(response.getStatus().toString());

        return statusResponse;
    }

    @Override
    public StatusResponse requestVerificationEmail(String email) {
        var response = this.userClient.requestVerificationOtp(RequestOtpRequest.newBuilder().setEmail(email).build());
        var statusResponse = new StatusResponse();
        statusResponse.setStatus(response.getStatus().toString());

        return statusResponse;
    }

    @Override
    public StatusResponse validateVerificationOtp(String email, String otp) {
        var response = this.userClient.verifyOtp(VerifyOtpRequest.newBuilder().setOtp(otp).setEmail(email).build());
        var statusResponse = new StatusResponse();
        statusResponse.setStatus(response.getStatus().toString());

        return statusResponse;
    }

    @Override
    public List<UserDto> getAllUsers(GetUsersDto filter, int page, int size) {
        var dataFilter = User.newBuilder()
                .setEmail(filter.getEmail())
                .setLastName(filter.getFirst_name())
                .setFirstName(filter.getFirst_name())
                .setPhoneNumber(filter.getPhone_number())
                .setRole(filter.getRole())
                .build();

        var data = GetAllUsersRequest.newBuilder().setPage(page).setSize(size).setFilter(dataFilter).build();
        var response = this.userClient.getUsers(data);

        List<UserDto> userList = new ArrayList <>();
        var userBuild = new UserDto();

        response.getUsersList().forEach((user)->{
            userList.add(userBuilder(userBuild, user));
        });

        return userList;
    }

    private UserDto userBuilder(UserDto userBuild, User user) {
        userBuild.setId(user.getId());
        userBuild.setEmail(user.getEmail());
        userBuild.setRole(user.getRole());
        userBuild.setFirst_name(user.getFirstName());
        userBuild.setEmail_verified(user.getEmailVerified());
        userBuild.setLast_name(user.getLastName());
        userBuild.setPhone_number(user.getPhoneNumber());
        userBuild.setUpdated_at(String.valueOf(new Date(user.getUpdatedAt().getSeconds() * 1000L + user.getUpdatedAt().getNanos() / 1000000L)));
        userBuild.setCreated_at(String.valueOf(new Date(user.getCreatedAt().getSeconds() * 1000L + user.getCreatedAt().getNanos() / 1000000L)));

        return userBuild;
    }


}