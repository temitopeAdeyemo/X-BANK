package com.xbank.servicegateway.shared.annotations;

import com.xbank.servicegateway.shared.validators.RoleValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = RoleValidator.class)
public @interface Role {
    String message() default "Invalid role. Must be 'USER' or 'ADMIN'.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
