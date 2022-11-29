package com.liushengpei;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class HouseApplication {
    public static void main(String[] args) {
        SpringApplication.run(HouseApplication.class, args);
    }
}
