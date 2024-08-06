package com.xbank.servicegateway.shared.service;

import com.xbank.servicegateway.shared.Exceptions.UpstreamlServiceException;
import io.grpc.StatusRuntimeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import proto.getUser.proto.GetUserByUniqueFieldRequest;
import proto.getUser.proto.GetUserUniqueFields;
import proto.getUser.proto.User;
import proto.service.proto.AuthServiceGrpc;
import proto.service.proto.GetUserServiceGrpc;
import proto.service.proto.RegisterServiceGrpc;
import proto.user.proto.*;

@Service
public class UserClient {

    private final GetUserServiceGrpc.GetUserServiceBlockingStub synchronousGetUserClient;
    private final RegisterServiceGrpc.RegisterServiceBlockingStub synchronousRegisterServiceClient;
    private final AuthServiceGrpc.AuthServiceBlockingStub synchronousAuthServiceClient;

    @Autowired
    public UserClient(GetUserServiceGrpc.GetUserServiceBlockingStub synchronousGetUserClient,
                      RegisterServiceGrpc.RegisterServiceBlockingStub synchronousRegisterServiceClient, AuthServiceGrpc.AuthServiceBlockingStub synchronousAuthServiceClient){
        this.synchronousGetUserClient = synchronousGetUserClient;
        this.synchronousRegisterServiceClient = synchronousRegisterServiceClient;
        this.synchronousAuthServiceClient = synchronousAuthServiceClient;
    }

    public User getUserByUniqueField(String user_id){
        try {
            return this.synchronousGetUserClient.getUserByUniqueField(GetUserByUniqueFieldRequest.newBuilder().setFilterBy(GetUserUniqueFields.ID).setValue(user_id).build());
        }catch (Exception e){
            throw new UpstreamlServiceException(e.getMessage());
//            return User.newBuilder().build();
        }
    }

    public SignupResponse registerUser(SignupRequest data) {
        try {
            return this.synchronousRegisterServiceClient.registerUser(data);
        } catch (StatusRuntimeException e) {
            throw new UpstreamlServiceException(e.getStatus().getDescription());
//            return User.newBuilder().build();
        }
    }

        public LoginResponse loginUser(LoginRequest data){
            try {
                return this.synchronousAuthServiceClient.loginUser(data);
            }catch (StatusRuntimeException e){
                throw new UpstreamlServiceException(e.getStatus().getDescription());
//            return User.newBuilder().build();
            }
    }
}
