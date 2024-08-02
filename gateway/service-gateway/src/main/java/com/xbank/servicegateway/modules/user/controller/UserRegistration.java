package com.xbank.servicegateway.modules.user.controller;

import com.xbank.servicegateway.modules.user.service.UserService;
import com.xbank.servicegateway.shared.utils.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
public class UserRegistration {
    private final UserService userService;

//    public UserRegistration(UserService userService) {
//        this.userService = userService;
//    }

    ResponseEntity<ApiResponse> createUser(){
        this.userService.registerUser();
        return new ResponseEntity<>(new ApiResponse("User created Successfully."), HttpStatus.OK);
    }
}
