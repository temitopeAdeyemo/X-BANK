package com.xbank.servicegateway.shared.utils;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {
    @JsonProperty("timezone")
    public ZonedDateTime timezone;

    @JsonProperty("success")
    public boolean success;

    @JsonProperty("message")
    public String message;

    @JsonProperty("data")
    public  T data;

//    @JsonProperty("dataList")
//    public  List<T> dataList;

//    public ApiResponse( String message, List<T> dataList){
//        this.message = message;
//        this.success = true;
//        this.dataList = dataList;
//        this.timezone = ZonedDateTime.now(ZoneId.of("Z"));
//    }

    public ApiResponse( String message, T data){
        this.message = message;
        this.success = true;
        this.data = data;
        this.timezone = ZonedDateTime.now(ZoneId.of("Z"));
    }
}
