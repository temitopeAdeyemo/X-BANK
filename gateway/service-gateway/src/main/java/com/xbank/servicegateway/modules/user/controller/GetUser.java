package com.xbank.servicegateway.modules.user.controller;

import com.xbank.servicegateway.modules.user.dto.UserDto;
import com.xbank.servicegateway.modules.user.service.Impl.UserServiceImpl;
import com.xbank.servicegateway.shared.service.UserClient;
import com.xbank.servicegateway.shared.utils.ApiResponse;
import com.xbank.servicegateway.shared.utils.ContextKeys;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/user")
public class GetUser {
    private final UserServiceImpl userDetailsService;

    public GetUser(UserServiceImpl userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @GetMapping("/get/single/{id}")
    ResponseEntity<ApiResponse<UserDto>> getUser(@PathVariable String id){
        var userr = ContextKeys.user.get();
        System.out.println(userr);

        var user = this.userDetailsService.getUser(id);
        return new ResponseEntity<>(new ApiResponse<>( "ok", user), HttpStatus.OK);
    }
}