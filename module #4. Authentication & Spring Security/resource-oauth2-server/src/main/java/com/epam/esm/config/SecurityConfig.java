package com.epam.esm.config;

import com.epam.esm.exception.ErrorBody;
import com.epam.esm.exception.ErrorCodes;
import com.epam.esm.exception.ExceptionMessageI18n;
import com.fasterxml.jackson.databind.ObjectMapper;
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
        http

            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))

            .authorizeHttpRequests(request -> request
                // Guest
                .requestMatchers(HttpMethod.GET, "/api/certificates", "/api/certificates/**").permitAll()

                // User
                .requestMatchers(HttpMethod.GET, "/api/tags/**", "/api/tags").hasAuthority("SCOPE_tag.read")
                .requestMatchers(HttpMethod.GET, "/api/orders/users/*").hasAuthority("SCOPE_user.order.read")
                .requestMatchers(HttpMethod.POST,"/api/orders").hasAuthority("SCOPE_user.order.write")
                .requestMatchers(HttpMethod.GET, "/api/users", "/api/users/**").hasAuthority("SCOPE_user.read")

                // Admin
                .requestMatchers("/api/tags", "/api/tags/**").hasAuthority("SCOPE_tag.write")
                .requestMatchers("/api/users", "/api/users/**").hasAuthority("SCOPE_user.write")
                .requestMatchers("/api/certificates", "/api/certificates/**").hasAuthority("SCOPE_certificate.write")
                .requestMatchers("/api/orders", "/api/orders/**").hasAuthority("SCOPE_order.write")
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
        return (request, response, authException) -> response.sendRedirect(issuer);
    }

    private AccessDeniedHandler accessDeniedHandler() {
        return (request, response, accessDeniedException) -> {
            response.setContentType(MimeTypeUtils.APPLICATION_JSON_VALUE);
            response.setStatus(HttpStatus.FORBIDDEN.value());
            response.setCharacterEncoding("UTF-8");
            String message = i18n.toLocale(String.valueOf(ErrorCodes.ACCESS_DENIED));
            ObjectMapper objectMapper = new ObjectMapper();
            response.getWriter()
                .write(objectMapper.writeValueAsString(new ErrorBody(message, ErrorCodes.ACCESS_DENIED)));
        };
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
