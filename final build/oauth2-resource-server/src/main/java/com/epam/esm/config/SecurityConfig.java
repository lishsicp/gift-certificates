package com.epam.esm.config;

import com.epam.esm.exception.ErrorBody;
import com.epam.esm.exception.ErrorCodes;
import com.epam.esm.exception.ExceptionMessageI18n;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtDecoders;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.io.IOException;
import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final String issuer;
    private final JwtAuthenticationTokenConverter jwtAuthenticationTokenConverter;
    private final PermissionEvaluator permissionEvaluator;
    private final ExceptionMessageI18n i18n;

    public SecurityConfig(OAuth2ResourceServerProperties properties,
        JwtAuthenticationTokenConverter jwtAuthenticationTokenConverter, PermissionEvaluator permissionEvaluator,
        ExceptionMessageI18n i18n) {
        this.issuer = properties.getJwt().getIssuerUri();
        this.jwtAuthenticationTokenConverter = jwtAuthenticationTokenConverter;
        this.permissionEvaluator = permissionEvaluator;
        this.i18n = i18n;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        customize(http);
        http
            .csrf().disable()
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .exceptionHandling(exceptionHandling -> exceptionHandling
                .authenticationEntryPoint(unauthorizedHandler())
                .accessDeniedHandler(accessDeniedHandler()))
            .oauth2ResourceServer(
                oauth2 -> oauth2
                    .jwt(jwt -> jwt
                        .jwtAuthenticationConverter(jwtAuthenticationTokenConverter)));
        return http.build();
    }

    public void customize(HttpSecurity http) throws Exception {
        http.cors(cors -> {
            CorsConfigurationSource source = s -> {
                CorsConfiguration configuration = new CorsConfiguration();
                configuration.setAllowCredentials(true);
                configuration.setAllowedOriginPatterns(List.of("*:[3000,8080]"));
                configuration.setAllowedOrigins(
                    List.of("http://localhost:3000", "http://localhost:8080"));
                configuration.setAllowedHeaders(List.of("*"));
                configuration.setAllowedMethods(List.of("GET", "POST", "PATCH", "DELETE", "OPTIONS"));
                return configuration;
            };
            cors.configurationSource(source);
        });
    }

    @Bean
    public AuthenticationEntryPoint unauthorizedHandler() {
        return (request, response, authException) ->
            setExceptionBody(response, HttpStatus.UNAUTHORIZED, ErrorCodes.UNAUTHORIZED);
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return (request, response, accessDeniedException) ->
            setExceptionBody(response, HttpStatus.FORBIDDEN, ErrorCodes.ACCESS_DENIED);
    }

    private void setExceptionBody(
        HttpServletResponse response, HttpStatus httpStatus, int errorCode ) throws IOException {
        response.setContentType(MimeTypeUtils.APPLICATION_JSON_VALUE);
        response.setStatus(httpStatus.value());
        response.setCharacterEncoding("UTF-8");
        String message = i18n.toLocale(String.valueOf(errorCode));
        ObjectMapper objectMapper = new ObjectMapper();
        response.getWriter()
            .write(objectMapper.writeValueAsString(new ErrorBody(message, errorCode)));
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        return JwtDecoders.fromIssuerLocation(issuer);
    }

    @Bean
    public MethodSecurityExpressionHandler createExpressionHandler() {
        DefaultMethodSecurityExpressionHandler expressionHandler = new DefaultMethodSecurityExpressionHandler();
        expressionHandler.setPermissionEvaluator(permissionEvaluator);
        return expressionHandler;
    }

}
