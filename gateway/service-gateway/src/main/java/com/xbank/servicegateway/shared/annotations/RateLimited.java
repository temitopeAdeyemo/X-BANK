package com.xbank.servicegateway.shared.annotations;

import org.redisson.api.RateIntervalUnit;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface RateLimited {
    String endpoint();
    long interval() default 10;

    int point() default 5;
    RateIntervalUnit unit() default RateIntervalUnit.SECONDS;
}