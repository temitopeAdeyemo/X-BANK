package com.xbank.servicegateway.shared.validators;

import com.xbank.servicegateway.shared.annotations.Role;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class RoleValidator implements ConstraintValidator<Role, String> {

    @Override
    public void initialize(Role constraintAnnotation) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return "USER".equals(value) || "ADMIN".equals(value);
    }
}
