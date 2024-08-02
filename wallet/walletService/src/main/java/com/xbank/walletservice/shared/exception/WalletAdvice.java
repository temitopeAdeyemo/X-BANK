package com.xbank.walletservice.shared.exception;

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
public class WalletAdvice {
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
                                        .setDomain("com.x-bank.walletService")
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
                            .setDomain("com.x-bank.walletService")
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
                                        .setDomain("com.x-bank.walletService")
                                .build()
                        )
                )
                .build();

        return StatusProto.toStatusRuntimeException(status);
    }

    @GrpcExceptionHandler(EntityNotFoundException.class)
    public static StatusRuntimeException handleEntityNotFoundException(EntityNotFoundException ex){
        Status status = Status
                .newBuilder().setMessage(ex.getMessage()).
                setCode(Code.NOT_FOUND_VALUE)
                .addDetails(
                        Any.pack(
                                ErrorInfo.newBuilder()
                                        .setReason("NOT_FOUND")
                                        .setDomain("com.x-bank.walletService")
                                        .build()
                        )
                )
                .build();

        return StatusProto.toStatusRuntimeException(status);
    }

    @GrpcExceptionHandler(InsufficientBalanceException.class)
    public static StatusRuntimeException handleInsufficientBalanceException(InsufficientBalanceException ex){
        var msg = ex.getMessage().isEmpty()? "Insufficient Balance.": ex.getMessage();
        Status status = Status
                .newBuilder().setMessage(msg).
                setCode(Code.FAILED_PRECONDITION_VALUE)
                .addDetails(
                        Any.pack(
                                ErrorInfo.newBuilder()
                                        .setReason("NOT_FOUND")
                                        .setDomain("com.x-bank.walletService")
                                        .build()
                        )
                )
                .build();

        return StatusProto.toStatusRuntimeException(status);
    }

    @GrpcExceptionHandler(UpstreamlServiceException.class)
    public static StatusRuntimeException handleUpstreamlServiceExceptionn(UpstreamlServiceException ex){
        var msg = ex.getMessage().isEmpty()? ex.getMessage(): "Could not handle user request.";
        Status status = Status
                .newBuilder().setMessage(msg).
                setCode(Code.FAILED_PRECONDITION_VALUE)
                .addDetails(
                        Any.pack(
                                ErrorInfo.newBuilder()
                                        .setReason(msg.isEmpty()? "UPSTREAM_ERROR": ex.getMessage())
                                        .setDomain("com.x-bank.walletService")
                                        .build()
                        )
                )
                .build();

        return StatusProto.toStatusRuntimeException(status);
    }

    @GrpcExceptionHandler(CredentialExistsException.class)
    public static StatusRuntimeException handleCredentialExistsException(CredentialExistsException ex){
        var msg = ex.getMessage().isEmpty()? ex.getMessage(): "Credential exists.";
        Status status = Status
                .newBuilder().setMessage(msg).
                setCode(Code.ALREADY_EXISTS_VALUE)
                .addDetails(
                        Any.pack(
                                ErrorInfo.newBuilder()
                                        .setReason(msg.isEmpty()? "Credential exists": ex.getMessage())
                                        .setDomain("com.x-bank.walletService")
                                        .build()
                        )
                )
                .build();

        return StatusProto.toStatusRuntimeException(status);
    }

    @GrpcExceptionHandler(TransactionDeclinedException.class)
    public static StatusRuntimeException handleTransactionDeclinedException(TransactionDeclinedException ex){
        var msg = ex.getMessage().isEmpty()? "Invalid amount.": ex.getMessage();
        Status status = Status
                .newBuilder().setMessage(msg).
                setCode(Code.FAILED_PRECONDITION_VALUE)
                .addDetails(
                        Any.pack(
                                ErrorInfo.newBuilder()
                                        .setReason("INVALID_AMOUNT")
                                        .setDomain("com.x-bank.walletService")
                                        .build()
                        )
                )
                .build();

        return StatusProto.toStatusRuntimeException(status);
    }

    @GrpcExceptionHandler(Exception.class)
    public static StatusRuntimeException handleAllExceptions(Exception ex) {
       String defaultErrorMessage =  ex.getMessage() != null? ex.getMessage():"SERVER ERROR";
        System.out.println(defaultErrorMessage);
        System.out.println(Arrays.toString(ex.getStackTrace()));
        Status status = Status
                .newBuilder().setMessage("SERVER ERROR!").
                setCode(Code.INTERNAL_VALUE)
                .addDetails(
                        Any.pack(
                                ErrorInfo.newBuilder()
                                        .setReason("SERVER_ERROR")
                                        .setDomain("com.x-bank.walletService")
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
                                        .setDomain("com.x-bank.walletService")
                                        .build()
                        )
                )
                .build();

        return StatusProto.toStatusRuntimeException(status);
    }
}

