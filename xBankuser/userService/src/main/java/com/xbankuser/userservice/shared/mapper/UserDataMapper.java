package com.xbankuser.userservice.shared.mapper;

import com.google.protobuf.Timestamp;
import proto.getUser.proto.User;

public class UserDataMapper {
    public static User  mapUserToProtobuf(com.xbankuser.userservice.modules.auth.entiy.User userInfo){
        boolean emailVerified = userInfo.getEmailVerified() != null && userInfo.getEmailVerified();

        return User.newBuilder()
                .setId(String.valueOf(userInfo.getId()))
                .setEmail(userInfo.getEmail())
                .setPhoneNumber(userInfo.getPhoneNumber())
                .setEmailVerified(emailVerified)
                .setRole(userInfo.getRole().name())
                .setFirstName(userInfo.getFirstName())
                .setLastName(userInfo.getLastName())
                .setCreatedAt(userInfo.getCreatedAt() != null ? Timestamp.newBuilder().setSeconds(userInfo.getCreatedAt().toInstant().getEpochSecond()).build() : Timestamp.getDefaultInstance())
                .setUpdatedAt(userInfo.getUpdatedAt() != null ? Timestamp.newBuilder().setSeconds(userInfo.getUpdatedAt().toInstant().getEpochSecond()).build() : Timestamp.getDefaultInstance())
                .build();
    }
}
