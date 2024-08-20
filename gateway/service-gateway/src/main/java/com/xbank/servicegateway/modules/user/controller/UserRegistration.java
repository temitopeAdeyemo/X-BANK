package com.xbank.servicegateway.modules.user.controller;

import com.xbank.servicegateway.modules.user.dto.UserRegistrationDataMapper;
import com.xbank.servicegateway.modules.user.dto.UserRegistrationDto;
import com.xbank.servicegateway.modules.user.service.UserService;
import com.xbank.servicegateway.shared.utils.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")

public class UserRegistration {
    private final UserService userService;

//    @RateLimited(endpoint = "/register", interval = 5, point = 10)
    @PostMapping("/register")
    ResponseEntity <ApiResponse<UserRegistrationDataMapper>> createUser(@RequestBody() @Valid UserRegistrationDto data){

        var response = this.userService.registerUser(data);

        return new ResponseEntity<>(new ApiResponse<>("User created Successfully.", response), HttpStatus.CREATED);
    }
}