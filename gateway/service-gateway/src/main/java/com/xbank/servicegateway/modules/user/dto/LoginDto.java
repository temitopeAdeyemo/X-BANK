package com.xbank.servicegateway.modules.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class LoginDto {
    @Email
    @NotNull
    private String email;

    @NotBlank
    @Pattern(regexp =  "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$",
            message = "Password must be at least 8 characters long and contain at least one uppercase letter, one number, and one special character")
    private String password;
}