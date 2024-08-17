package com.xbank.servicegateway.config.GrpcClientConfig;

import com.xbank.servicegateway.shared.interceptor.HeaderClientInterceptor;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import proto.service.proto.GetUserServiceGrpc;
import proto.service.proto.AuthServiceGrpc;
import proto.service.proto.RegisterServiceGrpc;
import proto.service.proto.RequestAuthenticatorGrpc;

@Configuration
public class UserServiceClient {
    @Bean
    ManagedChannel userChannel (){
        return ManagedChannelBuilder.forAddress("localhost", 2022 ).usePlaintext().build();
    }

    @Bean
    GetUserServiceGrpc.GetUserServiceBlockingStub getUserService(ManagedChannel managedChannel){
        return GetUserServiceGrpc.newBlockingStub(managedChannel).withInterceptors().withInterceptors(new HeaderClientInterceptor());
    }

    @Bean
    RegisterServiceGrpc.RegisterServiceBlockingStub registerUser(ManagedChannel managedChannel){
        return RegisterServiceGrpc.newBlockingStub(managedChannel);
    }

    @Bean
    RequestAuthenticatorGrpc.RequestAuthenticatorBlockingStub requestAuthenticator(ManagedChannel managedChannel){
        return RequestAuthenticatorGrpc.newBlockingStub(managedChannel).withInterceptors().withInterceptors(new HeaderClientInterceptor());
    }

    @Bean
    AuthServiceGrpc.AuthServiceBlockingStub loginUser(ManagedChannel managedChannel){
        return AuthServiceGrpc.newBlockingStub(managedChannel);
    }
}
