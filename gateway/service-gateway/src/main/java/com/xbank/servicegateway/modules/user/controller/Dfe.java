package com.xbank.servicegateway.modules.user.controller;

import com.xbank.servicegateway.modules.user.dto.UserDto;
import com.xbank.servicegateway.shared.annotations.RateLimited;
import com.xbank.servicegateway.shared.utils.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/auth")
public class Dfe {
    @RateLimited(endpoint = "/api/v1/auth/c", interval = 15, point = 3)
    @GetMapping("/")
    ResponseEntity<ApiResponse<List<UserDto>>> getUseree(){
        System.out.println(":::::::::::::::::::::::::::::::::::");
        return new ResponseEntity<>(new ApiResponse<>( "Users fetched successfully", null), HttpStatus.OK);
    }
}
