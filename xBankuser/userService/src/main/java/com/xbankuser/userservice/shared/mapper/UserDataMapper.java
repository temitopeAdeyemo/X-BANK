package com.xbankuser.userservice.shared.mapper;

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
                .setCreatedAt(String.valueOf(userInfo.getCreatedAt()))
                .setUpdatedAt(String.valueOf(userInfo.getUpdatedAt()))
                .build();
    }
}
