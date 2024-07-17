package com.xbankuser.userservice.config;

import com.xbankuser.userservice.shared.Interceptor.GrpcAuthInterceptor;
import io.grpc.ServerInterceptor;
import net.devh.boot.grpc.server.interceptor.GrpcGlobalServerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GrpcConfig {

//    @Bean
//    @GrpcGlobalServerInterceptor
//    public ServerInterceptor authInterceptor() {
//        return new GrpcAuthInterceptor();
//    }
}
