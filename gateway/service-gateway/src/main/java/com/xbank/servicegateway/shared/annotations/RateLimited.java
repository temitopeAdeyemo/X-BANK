package com.xbank.servicegateway.shared.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.time.Duration;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RateLimited {
    String name() default "default";
    int limitForPeriod() default 10;
    String limitRefreshPeriod() default "15";
    String timeoutDuration() default "200";
}
