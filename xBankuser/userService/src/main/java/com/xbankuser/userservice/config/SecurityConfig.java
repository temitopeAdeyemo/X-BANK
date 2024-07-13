package com.xbankuser.userservice.config;

import com.xbankuser.userservice.modules.auth.repository.UserRepository;
import com.xbankuser.userservice.shared.service.Jwt.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.security.authentication.BearerAuthenticationReader;
import net.devh.boot.grpc.server.security.authentication.GrpcAuthenticationReader;
import net.devh.boot.grpc.server.security.check.AccessPredicate;
import net.devh.boot.grpc.server.security.check.AccessPredicateVoter;
import net.devh.boot.grpc.server.security.check.GrpcSecurityMetadataSource;
import net.devh.boot.grpc.server.security.check.ManualGrpcSecurityMetadataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.vote.UnanimousBased;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import proto.service.proto.AuthServiceGrpc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtAuthProvider jwtAuthProvider;
    private final JwtService jwtService;

    @Bean
    public PasswordEncoder passwordEncoder()
    {
        return new BCryptPasswordEncoder();
    }

    @Bean
    GrpcAuthenticationReader grpcAuthenticationReader (){
        return new BearerAuthenticationReader(token-> {
            var claims = this.jwtService.extractAllClaims(token);

            List<SimpleGrantedAuthority> authorities = Arrays.stream(claims.get("auth").toString().split(","))
                    .map((SimpleGrantedAuthority::new))
                    .collect(Collectors.toList());

            User user =  new User(claims.getSubject(), "", authorities);

            return new UsernamePasswordAuthenticationToken(user, token, authorities);
        });
    }

    @Bean
    AuthenticationManager authenticationManager(){
        return new ProviderManager(jwtAuthProvider);
    }
    @Bean
    GrpcSecurityMetadataSource grpcSecurityMetadataSource(){
        ManualGrpcSecurityMetadataSource manualGrpcSecurityMetadataSource  = new ManualGrpcSecurityMetadataSource();
        manualGrpcSecurityMetadataSource.setDefault(AccessPredicate.permitAll());
        manualGrpcSecurityMetadataSource.set(AuthServiceGrpc.getSayHelloUserMethod(), AccessPredicate.authenticated());
        return manualGrpcSecurityMetadataSource;
    }

    @Bean
    UnanimousBased accessDecisionManager (){
        List<AccessDecisionVoter<?>> accessDecisionVoters = new ArrayList<>();

        accessDecisionVoters.add(new AccessPredicateVoter());

        return new UnanimousBased(accessDecisionVoters);
    }
}
