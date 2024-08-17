package com.xbank.servicegateway.modules.user.dto;

import lombok.Data;

@Data
public class UserDto {
    String id;
    String phone_number;
    String email;
    String last_name;
    String first_name;
    String role;
    Boolean email_verified ;
    String created_at;
    String updated_at;
}
