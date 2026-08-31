package com.proyecto.integrador.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "cors")
public class Cors {
    String allowOrigins;
    String allowMethods;
    String allowHeaders;
    String exposedHeaders;
    int maxAge;
    boolean allowCredentials;
    String mapping;
}
