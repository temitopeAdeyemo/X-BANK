package com.xbank.servicegateway.config;

import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.security.authentication.BearerAuthenticationReader;
import net.devh.boot.grpc.server.security.authentication.GrpcAuthenticationReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;


@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {
    @Bean
    // This bean is used by the server interceptor
    GrpcAuthenticationReader grpcAuthenticationReader (){
        return new BearerAuthenticationReader(token-> new UsernamePasswordAuthenticationToken(null, token, null));
    }
}
