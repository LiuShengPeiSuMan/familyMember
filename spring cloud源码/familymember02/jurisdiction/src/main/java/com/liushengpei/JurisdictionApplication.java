package com.liushengpei;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class JurisdictionApplication {
    public static void main(String[] args) {
        SpringApplication.run(JurisdictionApplication.class, args);
    }
}
