package com.xbank.servicegateway.config.GrpcClientConfig;

import com.xbank.servicegateway.shared.interceptor.HeaderClientInterceptor;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import proto.service.proto.*;

@Configuration
public class WalletServiceConfig {
    @Bean
    ManagedChannel walletChannel(){
        return ManagedChannelBuilder.forAddress("localhost", 50051).usePlaintext().build();
    }

    @Bean
    WalletServiceGrpc.WalletServiceBlockingStub walletServiceBlockingStub(ManagedChannel managedChannel){
        return WalletServiceGrpc.newBlockingStub(managedChannel).withInterceptors().withInterceptors(new HeaderClientInterceptor());
    }

    @Bean
    GetWalletServiceGrpc.GetWalletServiceBlockingStub getWalletServiceBlockingStub(ManagedChannel managedChannel){
        return GetWalletServiceGrpc.newBlockingStub(managedChannel).withInterceptors().withInterceptors(new HeaderClientInterceptor());
    }

    @Bean

    DeleteWalletServiceGrpc.DeleteWalletServiceBlockingStub deleteWalletServiceBlockingStub(ManagedChannel managedChannel){
        return DeleteWalletServiceGrpc.newBlockingStub(managedChannel).withInterceptors().withInterceptors(new HeaderClientInterceptor());
    }

    @Bean
    TransactionInitServiceGrpc.TransactionInitServiceBlockingStub transactionInitServiceBlockingStub(ManagedChannel managedChannel){
        return TransactionInitServiceGrpc.newBlockingStub(managedChannel).withInterceptors().withInterceptors(new HeaderClientInterceptor());
    }

    @Bean
    GetTransactionServiceGrpc.GetTransactionServiceBlockingStub getTransactionServiceBlockingStub(ManagedChannel managedChannel){
        return GetTransactionServiceGrpc.newBlockingStub(managedChannel).withInterceptors().withInterceptors(new HeaderClientInterceptor());
    }
}
