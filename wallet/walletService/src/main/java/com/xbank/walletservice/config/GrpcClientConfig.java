package com.xbank.walletservice.config;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import proto.service.proto.GetUserServiceGrpc;

@Configuration
public class GrpcClientConfig {

    @Bean
    public ManagedChannel managedChannel() {
        return ManagedChannelBuilder.forAddress("0.0.0.0", 2022)
                .usePlaintext()
                .build();
    }

    @Bean
    public GetUserServiceGrpc.GetUserServiceBlockingStub userServiceBlockingStub(ManagedChannel managedChannel) {
        return GetUserServiceGrpc.newBlockingStub(managedChannel);
    }

    @Bean
    public GetUserServiceGrpc.GetUserServiceStub asyncUserClient(ManagedChannel managedChannel) {
        return GetUserServiceGrpc.newStub(managedChannel);
    }
}