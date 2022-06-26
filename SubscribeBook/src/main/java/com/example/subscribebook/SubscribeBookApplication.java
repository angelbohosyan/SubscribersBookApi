package com.example.subscribebook;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
@SpringBootApplication(exclude = {UserDetailsServiceAutoConfiguration.class },
        scanBasePackages = "com.example.subscribebook")
public class SubscribeBookApplication {

    public static void main(String[] args) {
        SpringApplication.run(SubscribeBookApplication.class, args);
    }

}
