package com.liushengpei;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class FamilyApplication {
    public static void main(String[] args) {
        SpringApplication.run(FamilyApplication.class, args);
    }
}
