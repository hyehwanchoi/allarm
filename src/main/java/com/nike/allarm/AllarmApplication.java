package com.nike.allarm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class AllarmApplication {

    public static void main(String[] args) {
        SpringApplication.run(AllarmApplication.class, args);
    }

}
