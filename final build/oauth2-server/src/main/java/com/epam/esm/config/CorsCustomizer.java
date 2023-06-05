package com.epam.esm.config;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.stereotype.Component;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.List;

@Component
public class CorsCustomizer {

    public void customize(HttpSecurity http) throws Exception {
        http.cors(cors -> {
            CorsConfigurationSource source = s -> {
                CorsConfiguration configuration = new CorsConfiguration();
                configuration.setAllowCredentials(true);
                configuration.setAllowedOriginPatterns(List.of("*:[3000,8080,8081,8082]"));
                configuration.setAllowedOrigins(
                    List.of("http://localhost:3000"));
                configuration.setAllowedHeaders(List.of("*"));
                configuration.setAllowedMethods(List.of("GET", "POST", "PATCH", "OPTIONS"));
                return configuration;
            };
            cors.configurationSource(source);
        });
    }
}
