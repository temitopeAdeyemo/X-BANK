package com.xbank.servicegateway.shared.validators;

import com.xbank.servicegateway.shared.annotations.ValidBoolean;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class BooleanValidator implements ConstraintValidator<ValidBoolean, Boolean> {
    @Override
    public boolean isValid(Boolean value, ConstraintValidatorContext constraintValidatorContext) {
        if (value == null) return false;

        return value.equals(Boolean.TRUE) || value.equals(Boolean.FALSE);
    }
}
