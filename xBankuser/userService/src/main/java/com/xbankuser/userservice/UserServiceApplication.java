package com.xbankuser.userservice;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import java.io.IOException;

@SpringBootApplication
@EnableCaching
public class UserServiceApplication {
    public static void main(String[] args) {

        SpringApplication.run(UserServiceApplication.class, args);
    }
}