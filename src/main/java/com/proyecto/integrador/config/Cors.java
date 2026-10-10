package com.proyecto.integrador.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.time.Duration;
import java.util.Arrays;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "cors")
public class Cors {

    private String allowOrigins;
    private String allowMethods;
    private String allowHeaders;
    private String exposedHeaders;
    private int maxAge;
    private boolean allowCredentials;
    private String mapping;

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowCredentials(allowCredentials);
        configuration.setAllowedOrigins(separarValores(allowOrigins));
        configuration.setAllowedMethods(separarValores(allowMethods));
        configuration.setAllowedHeaders(separarValores(allowHeaders));
        configuration.setExposedHeaders(separarValores(exposedHeaders));
        configuration.setMaxAge(Duration.ofSeconds(maxAge));

        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration(mapping, configuration);

        return source;
    }

    private java.util.List<String> separarValores(String valores) {
        return Arrays.stream(valores.split(","))
                .map(String::trim)
                .toList();
    }
}