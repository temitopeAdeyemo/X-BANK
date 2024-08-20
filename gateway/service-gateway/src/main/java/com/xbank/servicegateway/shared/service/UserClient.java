package com.xbank.servicegateway.shared.service;

import com.xbank.servicegateway.shared.Exceptions.UpstreamServiceException;
import io.grpc.StatusRuntimeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import proto.getUser.proto.*;
import proto.service.proto.*;
import proto.updateUser.proto.UpdateUserRequest;
import proto.updateUser.proto.UpdateUserResponse;
import proto.user.proto.*;
import proto.verifyEmail.proto.RequestOtpRequest;
import proto.verifyEmail.proto.RequestOtpResponse;
import proto.verifyEmail.proto.VerifyOtpRequest;
import proto.verifyEmail.proto.VerifyOtpResponse;

@Service
public class UserClient {
    private final GetUserServiceGrpc.GetUserServiceBlockingStub synchronousGetUserClient;
    private final RegisterServiceGrpc.RegisterServiceBlockingStub synchronousRegisterServiceClient;
    private final AuthServiceGrpc.AuthServiceBlockingStub synchronousAuthServiceClient;
    private final RequestAuthenticatorGrpc.RequestAuthenticatorBlockingStub synchronousRequestAuthenticator;
    private final VerifyEmailServiceGrpc.VerifyEmailServiceBlockingStub verifyEmailServiceBlockingStub;

    private final UpdateUserServiceGrpc.UpdateUserServiceBlockingStub updateUserServiceBlockingStub;

    @Autowired
    public UserClient(GetUserServiceGrpc.GetUserServiceBlockingStub synchronousGetUserClient,
                      RegisterServiceGrpc.RegisterServiceBlockingStub synchronousRegisterServiceClient,
                      AuthServiceGrpc.AuthServiceBlockingStub synchronousAuthServiceClient,
                      RequestAuthenticatorGrpc.RequestAuthenticatorBlockingStub synchronousRequestAuthenticator,
                      VerifyEmailServiceGrpc.VerifyEmailServiceBlockingStub verifyEmailServiceBlockingStub,
                      UpdateUserServiceGrpc.UpdateUserServiceBlockingStub updateUserServiceBlockingStub){
        this.synchronousGetUserClient = synchronousGetUserClient;
        this.synchronousRegisterServiceClient = synchronousRegisterServiceClient;
        this.synchronousAuthServiceClient = synchronousAuthServiceClient;
        this.synchronousRequestAuthenticator = synchronousRequestAuthenticator;
        this.verifyEmailServiceBlockingStub = verifyEmailServiceBlockingStub;
        this.updateUserServiceBlockingStub = updateUserServiceBlockingStub;
    }

    public User getUserByUniqueField(String user_id){
        try {
            return this.synchronousGetUserClient.getUserByUniqueField(GetUserByUniqueFieldRequest.newBuilder().setFilterBy(GetUserUniqueFields.ID).setValue(user_id).build());
        }catch (Exception e){
            throw new UpstreamServiceException(e.getMessage());
        }
    }

    public UpdateUserResponse updateUser(UpdateUserRequest updateUserRequest){
        try {
            return this.updateUserServiceBlockingStub.updateUser(updateUserRequest);
        }catch (Exception e){
            throw new UpstreamServiceException(e.getMessage());
        }
    }

    public GetAllUsersResponse getUsers(GetAllUsersRequest getAllUsersRequest){
        try {
            return this.synchronousGetUserClient.getAllUsers(getAllUsersRequest);
        }catch (Exception e){
            throw new UpstreamServiceException(e.getMessage());
        }
    }

    public SignupResponse registerUser(SignupRequest data) {
        try {
            return this.synchronousRegisterServiceClient.registerUser(data);
        } catch (StatusRuntimeException e) {
            throw new UpstreamServiceException(e.getStatus().getDescription());
        }
    }

        public LoginResponse loginUser(LoginRequest data){
            try {
                return this.synchronousAuthServiceClient.loginUser(data);
            }catch (StatusRuntimeException e){
                throw new UpstreamServiceException(e.getStatus().getDescription());
            }
    }

    public User authenticateUser() throws UpstreamServiceException{
        try {
            return this.synchronousRequestAuthenticator.authenticateUser(Empty.newBuilder().build());

        }catch (StatusRuntimeException e){
            throw new UpstreamServiceException(e.getStatus().getDescription());
        }
    }

    public RequestOtpResponse requestVerificationOtp(RequestOtpRequest data){
        try{
            return this.verifyEmailServiceBlockingStub.requestOtp(data);
        }catch (StatusRuntimeException e){
            throw new UpstreamServiceException(e.getStatus().getDescription());
        }
    }

    public VerifyOtpResponse verifyOtp(VerifyOtpRequest data){
        try{
            return this.verifyEmailServiceBlockingStub.verifyOtp(data);
        }catch (StatusRuntimeException e){
            throw new UpstreamServiceException(e.getStatus().getDescription());
        }
    }
}
