package com.epam.esm.config;

import com.epam.esm.exception.ErrorBody;
import com.epam.esm.exception.ErrorCodes;
import com.epam.esm.exception.ExceptionMessageI18n;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
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

import java.io.IOException;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private static final String[] TAG_ENDPOINTS = {"/api/tags", "/api/tags/**"};
    private static final String[] USER_ENDPOINTS = {"/api/users", "/api/users/**"};
    private static final String[] CERTIFICATE_ENDPOINTS = {"/api/certificates", "/api/certificates/**"};
    private static final String[] ORDER_ENDPOINTS = {"/api/orders", "/api/orders/**"};
    private static final String ADMIN = "ADMIN";
    private static final String USER_ORDERS_ENDPOINT = "/api/orders/users/*";

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
        http

            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))

            .authorizeHttpRequests(request -> request
                // Guest
                .requestMatchers(HttpMethod.GET, CERTIFICATE_ENDPOINTS).permitAll()

                // User
                .requestMatchers(HttpMethod.GET, TAG_ENDPOINTS).authenticated()
                .requestMatchers(HttpMethod.GET, USER_ENDPOINTS).hasAuthority("SCOPE_user.read")
                .requestMatchers(HttpMethod.GET, USER_ORDERS_ENDPOINT).hasAuthority("SCOPE_user.order.read")
                .requestMatchers(HttpMethod.POST,ORDER_ENDPOINTS).hasAnyAuthority("SCOPE_user.order.write", "SCOPE_order.write")

                // Admin
                .anyRequest().hasRole(ADMIN)
            )

            .exceptionHandling(ex -> ex
                .authenticationEntryPoint(authenticationEntryPoint())
                .accessDeniedHandler(accessDeniedHandler()))

            .oauth2ResourceServer(
                oauth2 -> oauth2
                    .jwt(jwt -> jwt
                        .jwtAuthenticationConverter(jwtAuthenticationTokenConverter)));

        return http.build();
    }

    private AuthenticationEntryPoint authenticationEntryPoint() {
         return (request, response, authException) ->
             setExceptionBody(response, HttpStatus.UNAUTHORIZED, ErrorCodes.UNAUTHORIZED);
    }

    private AccessDeniedHandler accessDeniedHandler() {
        return (request, response, accessDeniedException) ->
            setExceptionBody(response, HttpStatus.FORBIDDEN, ErrorCodes.ACCESS_DENIED);
    }

    private void setExceptionBody(HttpServletResponse response, HttpStatus httpStatus, int errorCode ) throws IOException {
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
