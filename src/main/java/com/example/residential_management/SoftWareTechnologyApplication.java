package com.example.residential_management;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class SoftWareTechnologyApplication {
    public static void main(String[] args) {
        SpringApplication.run(SoftWareTechnologyApplication.class, args);
    }
}