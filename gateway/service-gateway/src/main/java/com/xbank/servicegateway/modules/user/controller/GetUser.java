package com.xbank.servicegateway.modules.user.controller;

import com.xbank.servicegateway.modules.user.dto.GetUsersDto;
import com.xbank.servicegateway.modules.user.dto.UserDto;
import com.xbank.servicegateway.modules.user.service.Impl.UserServiceImpl;
import com.xbank.servicegateway.shared.utils.ApiResponse;
import com.xbank.servicegateway.shared.utils.ContextKeys;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user")
@CacheConfig(cacheNames = "user")
public class GetUser {
    private final UserServiceImpl userDetailsService;

    public GetUser(UserServiceImpl userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @GetMapping("/get/single/{id}")
    @Cacheable( key = "#id")
    public ResponseEntity<ApiResponse<UserDto>> getUser(@PathVariable String id){
        var user = this.userDetailsService.getUser(id);
        return new ResponseEntity<>(new ApiResponse<>( "User fetched successfully", user), HttpStatus.OK);
    }

    @GetMapping("/get/many")
    @Cacheable(value = "users")
    public ResponseEntity<ApiResponse<List<UserDto>>> getUser(@ModelAttribute GetUsersDto filter, @RequestParam("page") int page, @RequestParam("page") int size ){
        var user = this.userDetailsService.getAllUsers(filter, page, size);
        return new ResponseEntity<>(new ApiResponse<>( "Users fetched successfully", user), HttpStatus.OK);
    }
}