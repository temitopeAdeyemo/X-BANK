package com.xbank.servicegateway.modules.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class UserRegistrationDto {
    @Email
    @NotNull
    private String email;

    @NotNull
    @Pattern(regexp = "\\d+", message = "Phone number must be a number.")
    private String phone_number;

    @NotNull
    @Pattern(regexp =  "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$",
            message = "Password must be at least 8 characters long and contain at least one uppercase letter, one number, and one special character")
    private String password;

    @Pattern(regexp = "^[a-zA-Z\\s]+$", message = "Last name must contain only alphabetic characters and spaces")
    @NotBlank
    private String last_name;

    @NotBlank
    @Pattern(regexp = "^[a-zA-Z\\s]+$", message = "First Name must contain only alphabetic characters and spaces")
    private String first_name;
}
