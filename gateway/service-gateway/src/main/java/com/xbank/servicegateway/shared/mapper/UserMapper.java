package com.xbank.servicegateway.shared.mapper;

import com.xbank.servicegateway.modules.user.dto.UserDto;
import proto.getUser.proto.User;

import java.util.Date;

public class UserMapper {
    public UserDto userBuilder(UserDto userBuild, User user) {
        userBuild.setId(user.getId());
        userBuild.setEmail(user.getEmail());
        userBuild.setRole(user.getRole());
        userBuild.setFirst_name(user.getFirstName());
        userBuild.setEmail_verified(user.getEmailVerified());
        userBuild.setLast_name(user.getLastName());
        userBuild.setPhone_number(user.getPhoneNumber());
        userBuild.setUpdated_at(convertProtoTimestampToDate(user.getUpdatedAt()));
        userBuild.setCreated_at(convertProtoTimestampToDate(user.getCreatedAt()));

        return userBuild;
    }

        private String convertProtoTimestampToDate(com.google.protobuf.Timestamp timestamp) {
        long millis = timestamp.getSeconds() * 1000L + timestamp.getNanos() / 1000000L;
        return String.valueOf(new Date(millis));
    }
}
