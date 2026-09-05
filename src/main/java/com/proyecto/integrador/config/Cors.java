package com.proyecto.integrador.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.time.Duration;
import java.util.Arrays;

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

    @Bean
    CorsFilter corsFilter() {
        var config = new CorsConfiguration();
        config.setAllowCredentials(this.isAllowCredentials());
        config.setAllowedOrigins(Arrays.asList(this.getAllowOrigins().split(",")));
        config.setAllowedHeaders(Arrays.asList(this.getAllowHeaders().split(",")));
        config.setAllowedMethods(Arrays.asList(this.getAllowMethods().split(",")));
        config.setMaxAge(Duration.ofSeconds(this.getMaxAge()));

        var source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration(this.getMapping(), config);

        return new CorsFilter(source);
    }
}
