package com.proyecto.integrador;

import com.proyecto.integrador.config.Cors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.time.Duration;
import java.util.Arrays;

@SpringBootApplication
public class WebIntegradoServiceApplication {

	@Autowired
	private Cors cors;

	public static void main(String[] args) {
		SpringApplication.run(WebIntegradoServiceApplication.class, args);
	}

	@Bean
	CorsFilter corsFilter() {
		var config = new CorsConfiguration();
		config.setAllowCredentials(cors.isAllowCredentials());
		config.setAllowedOrigins(Arrays.asList(cors.getAllowOrigins().split(",")));
		config.setAllowedHeaders(Arrays.asList(cors.getAllowHeaders().split(",")));
		config.setAllowedMethods(Arrays.asList(cors.getAllowMethods().split(",")));
		config.setMaxAge(Duration.ofSeconds(cors.getMaxAge()));

		var source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration(cors.getMapping(), config);

		return new CorsFilter(source);
	}
}
