package com.xbank.servicegateway.modules.user.controller;

import com.xbank.servicegateway.shared.utils.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/user")
public class UserRegistration {
    ResponseEntity<ApiResponse> createUser(){
        return new ResponseEntity<>(new ApiResponse("User created Successfully."), HttpStatus.OK);
    }
}
