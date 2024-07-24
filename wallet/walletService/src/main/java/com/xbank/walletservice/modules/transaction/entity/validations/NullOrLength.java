package com.xbank.walletservice.modules.transaction.entity.validations;

//import javax.validation.Constraint;
//import javax.validation.Payload;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = NullOrLengthValidator.class)
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface NullOrLength {
    String message() default "String must be either null, empty, or between {minLength} and {maxLength} characters long";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    int minLength() default 1;
    int maxLength() default 12;
}
