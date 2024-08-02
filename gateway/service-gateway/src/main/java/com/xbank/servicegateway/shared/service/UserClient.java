package com.xbank.servicegateway.shared.service;

import com.xbank.servicegateway.shared.Exceptions.UpstreamlServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import proto.getUser.proto.GetUserByUniqueFieldRequest;
import proto.getUser.proto.GetUserUniqueFields;
import proto.getUser.proto.User;
import proto.service.proto.GetUserServiceGrpc;
import proto.service.proto.RegisterServiceGrpc;
import proto.user.proto.SignupRequest;
import proto.user.proto.SignupRequestOrBuilder;
import proto.user.proto.SignupResponse;

@Service
public class UserClient {

    private final GetUserServiceGrpc.GetUserServiceBlockingStub synchronousGetUserClient;
    private final RegisterServiceGrpc.RegisterServiceBlockingStub synchronousRegisterServiceClient;

    @Autowired
    public UserClient(GetUserServiceGrpc.GetUserServiceBlockingStub synchronousGetUserClient,
                      RegisterServiceGrpc.RegisterServiceBlockingStub synchronousRegisterServiceClient){
        this.synchronousGetUserClient = synchronousGetUserClient;
        this.synchronousRegisterServiceClient = synchronousRegisterServiceClient;
    }

    public User getUserByUniqueField(String user_id){
        try {
            return this.synchronousGetUserClient.getUserByUniqueField(GetUserByUniqueFieldRequest.newBuilder().setFilterBy(GetUserUniqueFields.ID).setValue(user_id).build());
        }catch (Exception e){
            throw new UpstreamlServiceException(e.getMessage());
//            return User.newBuilder().build();
        }
    }

    public SignupResponse registerUser(SignupRequest data){
        try {
            return this.synchronousRegisterServiceClient.registerUser(data);
        }catch (Exception e){
            throw new UpstreamlServiceException(e.getMessage());
//            return User.newBuilder().build();
        }
    }
}
