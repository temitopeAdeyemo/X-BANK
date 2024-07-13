package com.xbankuser.userservice.shared.exception;

import com.google.protobuf.Any;
import com.google.rpc.Code;
import com.google.rpc.ErrorInfo;
import com.google.rpc.Status;
import io.grpc.StatusRuntimeException;
import io.grpc.protobuf.StatusProto;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.validation.ConstraintViolationException;
import net.devh.boot.grpc.server.advice.GrpcAdvice;
import net.devh.boot.grpc.server.advice.GrpcExceptionHandler;
import org.springframework.security.core.AuthenticationException;
import org.springframework.transaction.TransactionSystemException;

import java.util.Arrays;

@GrpcAdvice
public class UserAdvice {
    @GrpcExceptionHandler(UserNotFoundException.class)
    public static StatusRuntimeException handleUserNotFoundException(UserNotFoundException ex){
        Status status = Status
                .newBuilder()
                .setCode(Code.NOT_FOUND_VALUE)
                .addDetails(
                        Any.pack(
                                (ErrorInfo.newBuilder()
                                        .setReason(ex.getMessage())
                                        .setDomain("com.x-bank.userService")
                                        .build()
                                )
                        )
                )
                .build();

        return StatusProto.toStatusRuntimeException(status);
    }

    @GrpcExceptionHandler(AuthenticationException.class)
    public static StatusRuntimeException handleAuthenticationException(AuthenticationException ex){
        Status status = Status
                .newBuilder()
                .setMessage(ex.getMessage())
                .setCode(Code.UNAUTHENTICATED_VALUE)
                .addDetails(
                        Any.pack(
                                (ErrorInfo.newBuilder()
                                        .setReason(ex.getMessage())
                                        .setDomain("com.x-bank.userService")
                                        .build()
                                )
                        )
                )
                .build();

        return StatusProto.toStatusRuntimeException(status);
    }

    @GrpcExceptionHandler(JwtException.class)
    public static StatusRuntimeException handleJwtException(JwtException ex){
        String msg = ex.getMessage();
        if(ex.getClass().equals(SignatureException.class)) msg = "INVALID SIGNATURE / TOKEN.";
        if(ex.getClass().equals(ExpiredJwtException.class)) msg = "TOKEN EXPIRED.";

        Status status = Status
                .newBuilder()
                .setMessage(msg)
                .setCode(Code.UNAUTHENTICATED_VALUE)
                .addDetails(
                        Any.pack(
                                (ErrorInfo.newBuilder()
                                        .setReason(ex.getMessage())
                                        .setDomain("com.x-bank.userService")
                                        .build()
                                )
                        )
                )
                .build();

        return StatusProto.toStatusRuntimeException(status);
    }

    @GrpcExceptionHandler(CredentialExistsException.class)
    public static StatusRuntimeException handleCredentialExistsException(CredentialExistsException ex){
        Status status = Status
                .newBuilder()
                .setCode(Code.ALREADY_EXISTS_VALUE)
                .setMessage(ex.getMessage())
                .addDetails(
                        Any.pack(
                                (ErrorInfo.newBuilder()
                                        .setReason(ex.getMessage())
                                        .setDomain("com.x-bank.userService")
                                        .build()
                                )
                        )
                )
                .build();

        return StatusProto.toStatusRuntimeException(status);
    }

    @GrpcExceptionHandler(ServerErrorException.class)
    public static StatusRuntimeException handleServerErrorException(ServerErrorException ex){
        Status status = Status
                .newBuilder().setMessage("INTERNAL_SERVER_ERROR").
                setCode(Code.INTERNAL_VALUE)
                .addDetails(
                        Any.pack(
                                ErrorInfo.newBuilder()
                                        .setReason("INTERNAL_SERVER_ERROR")
                                        .setDomain("com.x-bank.userService")
                                .build()
                        )
                )
                .build();

        return StatusProto.toStatusRuntimeException(status);
    }

    @GrpcExceptionHandler(Exception.class)
    public static StatusRuntimeException handleAllExceptions(Exception ex) {
       String defaultErrorMessage =  ex.getMessage() != null? ex.getMessage():"SERVER ERROR";

        Status status = Status
                .newBuilder().setMessage("SERVER ERROR").
                setCode(Code.INTERNAL_VALUE)
                .addDetails(
                        Any.pack(
                                ErrorInfo.newBuilder()
                                        .setReason(defaultErrorMessage )
                                        .setDomain("com.x-bank.userService")
                                        .putMetadata("message", defaultErrorMessage)
                                        .build()
                        )
                )
                .build();

        return StatusProto.toStatusRuntimeException(status);
    }

    @GrpcExceptionHandler(TransactionSystemException.class)
    public StatusRuntimeException handleConstraintViolationException(TransactionSystemException ex) {
        Throwable cause = ex.getRootCause();

        String message = "";
        if (cause instanceof ConstraintViolationException) {
            var violation = ((ConstraintViolationException) cause).getConstraintViolations().iterator().next();
            message = violation.getPropertyPath() + ": " + violation.getMessage();

        }

        Status status = Status
                .newBuilder().setMessage(message).
                setCode(Code.INTERNAL_VALUE)
                .addDetails(
                        Any.pack(
                                ErrorInfo.newBuilder()
                                        .setReason("JPA ERROR")
                                        .setDomain("com.x-bank.userService")
                                        .build()
                        )
                )
                .build();

        return StatusProto.toStatusRuntimeException(status);
    }

    @GrpcExceptionHandler(UpstreamlServiceException.class)
    public StatusRuntimeException handleUpstreamServerException(UpstreamlServiceException ex) {
        var defaultErrorMessage =  ex.getMessage() != null? ex.getMessage():"SERVER ERROR";

        Status status = Status
                .newBuilder().setMessage(ex.getMessage()).
                setCode(Code.INTERNAL_VALUE)
                .addDetails(
                        Any.pack(
                                ErrorInfo.newBuilder()
                                        .setReason(defaultErrorMessage )
                                        .setDomain("com.x-bank.userService")
                                        .putMetadata("message", defaultErrorMessage)
                                        .build()
                        )
                )
                .build();

        return StatusProto.toStatusRuntimeException(status);
    }
}

