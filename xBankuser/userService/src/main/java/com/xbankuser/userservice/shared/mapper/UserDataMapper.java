package com.xbankuser.userservice.shared.mapper;

import proto.getUser.proto.User;

public class UserDataMapper {
    public static User  mapUserToProtobuf(com.xbankuser.userservice.modules.auth.entiy.User userInfo){
        return User.newBuilder()
                .setId(String.valueOf(userInfo.getId()))
                .setEmail(userInfo.getEmail())
                .setPhoneNumber(userInfo.getPhoneNumber())
                .setEmailVerified(userInfo.getEmailVerified())
                .setRole(userInfo.getRole().name())
                .setFirstName(userInfo.getFirstName())
                .setLastName(userInfo.getLastName())
                .build();
    }
}
