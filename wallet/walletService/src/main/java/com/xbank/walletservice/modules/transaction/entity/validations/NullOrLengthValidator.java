package com.xbank.walletservice.modules.transaction.entity.validations;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class NullOrLengthValidator implements ConstraintValidator<NullOrLength, String> {

    private int minLength;
    private int maxLength;

    @Override
    public void initialize(NullOrLength constraintAnnotation) {
        this.minLength = constraintAnnotation.minLength();
        this.maxLength = constraintAnnotation.maxLength();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        // Allow null and empty strings
        if (value == null || value.isEmpty()) {
            return true;
        }

        int length = value.length();
        return length >= minLength && length <= maxLength;
    }
}
