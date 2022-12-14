package com.liushengpei;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class HomePageApplication {
    public static void main(String[] args) {
        SpringApplication.run(HomePageApplication.class, args);
    }
}
