package com.epam.esm.config;

import com.epam.esm.jwt.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter authenticationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf().disable()
            .cors().disable()
            .httpBasic().disable()
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .authorizeHttpRequests()
            // Guest
            .requestMatchers(HttpMethod.GET, "/api/certificates", "/api/certificates/**").permitAll()
            // User
            .requestMatchers(HttpMethod.GET, "/api/orders/users/**").authenticated()
            .requestMatchers(HttpMethod.POST, "/api/orders/**").authenticated()
            .requestMatchers(HttpMethod.GET, "/api/tags/**", "/api/users/**").authenticated()
            // Admin
            .anyRequest().hasRole("ADMIN")
            .and()
            .addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
