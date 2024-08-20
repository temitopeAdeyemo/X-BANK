package com.xbank.servicegateway.modules.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class UpdateUserDto {
    @Email
    private String email;

    @Pattern(regexp = "\\d+", message = "Phone number must be a number.")
    private String phone_number;

    @Pattern(regexp = "^[a-zA-Z\\s]+$", message = "Last name must contain only alphabetic characters and spaces")
    private String last_name;

    @Pattern(regexp = "^[a-zA-Z\\s]+$", message = "First Name must contain only alphabetic characters and spaces")
    private String first_name;
}