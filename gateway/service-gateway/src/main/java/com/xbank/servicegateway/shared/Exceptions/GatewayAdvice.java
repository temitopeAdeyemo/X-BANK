package com.xbank.servicegateway.shared.Exceptions;

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
    public ResponseEntity<Object> handleApiException(ApiRequestException e){
        HttpStatus badRequest = HttpStatus.BAD_REQUEST;

        ApiException<Object> apiException = new ApiException<>(e.getMessage(), null);

        return new ResponseEntity<>(apiException, badRequest);
    }

    @ExceptionHandler(value = {UpstreamlServiceException.class})
    public ResponseEntity<Object> handleUpstreamlServiceException(UpstreamlServiceException e){
        HttpStatus badRequest = HttpStatus.BAD_REQUEST;

        ApiException<Object> apiException = new ApiException<>(e.getMessage(), null);

        return new ResponseEntity<>(apiException, badRequest);
    }


    @ResponseStatus(HttpStatus.BAD_REQUEST)

    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    public ResponseEntity<ApiException<Map<String, String>>> handleInvalidArgument(MethodArgumentNotValidException ex){
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
        return new ResponseEntity<>(new ApiException<>("Page Not Found", null), HttpStatus.NOT_FOUND);
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
