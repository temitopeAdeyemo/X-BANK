package com.xbankuser.userservice.modules.auth.service;

import com.xbankuser.userservice.config.JwtAuthProvider;
import com.xbankuser.userservice.shared.mapper.UserDataMapper;
import com.xbankuser.userservice.shared.service.Jwt.JwtService;
import com.xbankuser.userservice.shared.utils.ContextKeys;
import io.grpc.stub.StreamObserver;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import proto.getUser.proto.User;
import proto.service.proto.AuthServiceGrpc;
import proto.user.proto.*;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.stream.Collectors;

@GrpcService
@RequiredArgsConstructor
public class Auth extends AuthServiceGrpc.AuthServiceImplBase {
    private final JwtService jwtService;
    private final JwtAuthProvider jwtAuthProvider;

    @Override
    public void loginUser(LoginRequest request, StreamObserver<LoginResponse> responseObserver) {
        Instant now = Instant.now();
        Instant expiresAt = now.plus(1, ChronoUnit.HOURS);

        Authentication auth = this.jwtAuthProvider.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

        String authorities = auth.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(","));

        String token = Jwts.builder()
                .setSubject((String) auth.getPrincipal())
                .claim("auth", authorities)
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(expiresAt))
                .signWith(this.jwtService.getSignInKey(), SignatureAlgorithm.HS256)
                .compact();

        LoginResponse loginResponse = LoginResponse.newBuilder().setAccessToken(token).build();

        responseObserver.onNext(loginResponse);
        responseObserver.onCompleted();
    }

    @Override
    public void sayHelloUser(sayHelloUserRequest request, StreamObserver<sayHelloUserResponse> responseObserver) {
        responseObserver.onNext(sayHelloUserResponse.newBuilder().setMessage("Hello Word!!!").build());
        responseObserver.onCompleted();
    }

    @Override
    public void authenticateUser(Empty request, StreamObserver<User> responseObserver) {
        var user = ContextKeys.user.get();

        User userBuild = UserDataMapper.mapUserToProtobuf(user);

        responseObserver.onNext(userBuild);
        responseObserver.onCompleted();
    }
}