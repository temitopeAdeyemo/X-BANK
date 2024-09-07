package com.xbank.servicegateway.shared.Exceptions;

import com.xbank.servicegateway.shared.utils.ApiException;
import io.github.resilience4j.ratelimiter.RequestNotPermitted;
import jakarta.validation.constraints.Null;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GatewayAdvice {
    @ExceptionHandler(value = {ApiRequestException.class})
    public ResponseEntity<ApiException<Map<String, String>>> handleApiException(ApiRequestException e){
        System.out.println("ApiRequestException: " + e);
        HttpStatus badRequest = HttpStatus.BAD_REQUEST;
        ApiException<Map<String, String>> apiException = new ApiException<>(e.getMessage(), null);

        return new ResponseEntity<>(apiException, badRequest);
    }

    @ExceptionHandler(RequestNotPermitted.class)
    public ResponseEntity<ApiException<Map<String, String>>> handleRateLimitException(RequestNotPermitted ex) {
        System.out.println("Exception: " + ex);
        HttpStatus badRequest = HttpStatus.TOO_MANY_REQUESTS;
        ApiException<Map<String, String>> apiException = new ApiException<>(ex.getMessage(), null);

        return new ResponseEntity<>(apiException, badRequest);
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiException<Map<String, String>>> handleAllExceptions(Exception ex) {
        System.out.println("Exception: " + ex);
        HttpStatus badRequest = HttpStatus.BAD_REQUEST;
        ApiException<Map<String, String>> apiException = new ApiException<>(ex.getMessage(), null);

        return new ResponseEntity<>(apiException, badRequest);
    }

    @ExceptionHandler(value = {UpstreamServiceException.class})
    public ResponseEntity<ApiException<Null>> handleUpstreamServiceException(UpstreamServiceException e){
        System.out.println("UpstreamServiceException: " + e);
        HttpStatus badRequest = HttpStatus.BAD_REQUEST;
        ApiException<Null> apiException = new ApiException<>(e.getMessage(), null);

        return new ResponseEntity<>(apiException, badRequest);
    }

    @ExceptionHandler(value = {SystemBusyException.class})
    public ResponseEntity<ApiException<Null>> handleSystemBusyException(SystemBusyException e){
        System.out.println("SystemBusyException: " + e);
        HttpStatus status = HttpStatus.LOCKED;
        ApiException<Null> apiException = new ApiException<>(e.getMessage(), null);

        return new ResponseEntity<>(apiException, status);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)

    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    public ResponseEntity<ApiException<Map<String, String>>> handleInvalidArgument(MethodArgumentNotValidException ex){
        System.out.println("MethodArgumentNotValidException: " + ex);
        Map<String, String> errorMap = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(e -> {
            errorMap.put(e.getField(), e.getDefaultMessage());
        });

        ApiException<Map<String, String>> apiException = new ApiException<>("VALIDATION FAILED.", errorMap );

        return new ResponseEntity<>( apiException, HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)

    @ExceptionHandler(value = {HttpRequestMethodNotSupportedException.class})
    public ResponseEntity<ApiException<Object>> handleInvalidArgument(HttpRequestMethodNotSupportedException ex){
        return new ResponseEntity<>( new ApiException<>("Page Not Found", null), HttpStatus.NOT_FOUND);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)

    @ExceptionHandler(value = {ResourceNotFoundException.class})
    public ResponseEntity<ApiException<Object>> handlePathException(ResourceNotFoundException ex){
        return new ResponseEntity<>(new ApiException<>("Resource Not Found", null), HttpStatus.NOT_FOUND);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)

    @ExceptionHandler(value = {MethodArgumentTypeMismatchException.class})
    public ResponseEntity<ApiException<Object>> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex){
        return new ResponseEntity<>( new ApiException<>("Invalid Parameter", null), HttpStatus.NOT_FOUND);
    }

    @ResponseStatus(HttpStatus.TOO_MANY_REQUESTS)
    @ExceptionHandler(value = {RateLimitExceededException.class})
    public ResponseEntity<ApiException<Object>> handleRateLimitExceededException(RateLimitExceededException ex){
        return new ResponseEntity<>( new ApiException<>(ex.getMessage(), null), HttpStatus.TOO_MANY_REQUESTS);
    }
}
