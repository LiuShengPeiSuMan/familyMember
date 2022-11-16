package com.liushengpei;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class FamilyMemberApplication {
    public static void main(String[] args) {
        SpringApplication.run(FamilyMemberApplication.class,args);
    }
}
