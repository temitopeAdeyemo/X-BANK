package com.xbankuser.userservice.shared.Interceptor;

import com.xbankuser.userservice.modules.auth.entiy.User;
import com.xbankuser.userservice.shared.service.Jwt.JwtService;
import com.xbankuser.userservice.shared.utils.ContextKeys;
import io.grpc.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GrpcAuthInterceptor implements ServerInterceptor {

    private final JwtService jwtService;

    @Autowired
    public GrpcAuthInterceptor(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(
            ServerCall<ReqT, RespT> call,
            Metadata headers,
            ServerCallHandler<ReqT, RespT> next) {
        String token = headers.get(Metadata.Key.of("Authorization", Metadata.ASCII_STRING_MARSHALLER));

        User user = null;

//        assert token != null;
        if(token == null || token.isEmpty()){
            Context context = Context.current().withValue(ContextKeys.user, user);
            return Contexts.interceptCall(context, call, headers, next);
        }

        user = this.jwtService.extractUser(token);

        Context context = Context.current().withValue(ContextKeys.user, user);

        return Contexts.interceptCall(context, call, headers, next);
    }
}
