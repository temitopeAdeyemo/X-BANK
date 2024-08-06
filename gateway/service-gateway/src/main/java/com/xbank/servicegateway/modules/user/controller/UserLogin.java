package com.xbank.servicegateway.modules.user.controller;

import com.xbank.servicegateway.modules.user.dto.LoginDto;
import com.xbank.servicegateway.modules.user.dto.UserLoginDataMapper;
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
public class UserLogin {
    private final UserService userService;

    @PostMapping("/login")
    ResponseEntity <ApiResponse<UserLoginDataMapper>> login(@RequestBody @Valid LoginDto data){
        var response = this.userService.loginUser(data);

        return new ResponseEntity<>(new ApiResponse<>("User Logged In Successfully.", new UserLoginDataMapper(response.getAccessToken()) ), HttpStatus.OK);
    }
}

