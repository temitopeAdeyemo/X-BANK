package com.xbank.servicegateway.modules.user.controller;

import com.xbank.servicegateway.modules.user.dto.UpdateUserDto;
import com.xbank.servicegateway.modules.user.service.UserService;
import com.xbank.servicegateway.shared.utils.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UpdateUser {
    private final UserService userService;

    @PostMapping("/update")
    ResponseEntity<Object> init(@RequestBody @Valid UpdateUserDto data){
        var response = this.userService.updateUser(data);

        return new ResponseEntity<>(new ApiResponse<>( "Users updated successfully", response), HttpStatus.OK);
    }
}
