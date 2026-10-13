package com.proyecto.integrador;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;

@SpringBootApplication(
        exclude = UserDetailsServiceAutoConfiguration.class
)
public class WebIntegradoServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(
                WebIntegradoServiceApplication.class,
                args
        );
    }
}