package com.xbank.servicegateway.shared.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiException<T> {
    private final ZonedDateTime timezone;
    private final Boolean success;
    private final String message;
    private final T data;

    public ApiException( String message, /*Throwable throwable,*/  T data) {
        this.timezone = ZonedDateTime.now(ZoneId.of("Z"));
        this.success = false;
        this.message = message;

        this.data = data;

    }
}
