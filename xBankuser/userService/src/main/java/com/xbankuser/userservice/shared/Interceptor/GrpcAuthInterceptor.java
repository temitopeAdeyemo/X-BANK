package com.xbankuser.userservice.shared.Interceptor;

import com.xbankuser.userservice.shared.service.Jwt.JwtService;
import com.xbankuser.userservice.shared.utils.ContextKeys;
import io.grpc.*;
import org.springframework.stereotype.Component;

@Component
public class GrpcAuthInterceptor implements ServerInterceptor{
    private JwtService jwtService;
    @Override
    public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(
            ServerCall<ReqT, RespT> call,
            Metadata headers,
            ServerCallHandler<ReqT, RespT> next) {

        String authHeader = headers.get(Metadata.Key.of("Authorization", Metadata.ASCII_STRING_MARSHALLER));

        var user = this.jwtService.extractUser(authHeader);

        Context context = Context.current().withValue(ContextKeys.user, user);

        return Contexts.interceptCall(context, call, headers, next);
    }
}
