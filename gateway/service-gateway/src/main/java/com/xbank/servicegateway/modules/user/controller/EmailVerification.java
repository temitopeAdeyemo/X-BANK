package com.xbank.servicegateway.modules.user.controller;

import com.xbank.servicegateway.modules.user.service.UserService;
import com.xbank.servicegateway.shared.utils.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class EmailVerification {
    private final UserService userService;
    @PostMapping("verification/{email}")
    ResponseEntity<Object> init(@PathVariable String email){
        var response = this.userService.requestVerificationEmail(email);

        return new ResponseEntity<>(new ApiResponse<>( "Otp sent to your email.", response), HttpStatus.OK);
    }

    @PatchMapping("verification/{email}")
    ResponseEntity<Object> validate(@PathVariable String email, @RequestBody String otp){

        var response = this.userService.validateVerificationOtp(email, otp);

        return new ResponseEntity<>(new ApiResponse<>( "Users verified successfully", response), HttpStatus.OK);
    }
}
