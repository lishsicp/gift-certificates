package com.epam.esm.config;

import com.epam.esm.exception.EntityNotFoundException;
import com.epam.esm.exception.ExceptionResponseBody;
import com.epam.esm.idp.FederatedIdentityConfigurer;
import com.epam.esm.idp.OAuth2TokenClaimsCustomizer;
import com.epam.esm.idp.UserRepositoryOAuth2UserHandler;
import com.epam.esm.repository.AuthUserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.util.MimeTypeUtils;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
@EnableMethodSecurity
public class StandardSecurityConfig {

    private final CorsCustomizer corsCustomizer;
    private final AuthUserRepository authUserRepository;
    private final UserRepositoryOAuth2UserHandler auth2UserHandler;

    @Bean
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        corsCustomizer.customize(http);

        FederatedIdentityConfigurer federatedIdentityConfigurer =
            new FederatedIdentityConfigurer().oauth2UserHandler(auth2UserHandler);

        http.csrf(csrf -> csrf.ignoringRequestMatchers("/**"))
            .authorizeHttpRequests(authorize -> authorize.requestMatchers("/oauth2/**", "/login", "/webjars/**")
                .permitAll()
                .anyRequest()
                .authenticated())
            .exceptionHandling(ex -> ex.accessDeniedHandler(accessDeniedHandler()))
            .formLogin(formLogin -> formLogin.usernameParameter("email"));

        http.apply(federatedIdentityConfigurer);

        return http.build();
    }

    private AccessDeniedHandler accessDeniedHandler() {
        return (request, response, accessDeniedException) -> {
            response.setContentType(MimeTypeUtils.APPLICATION_JSON_VALUE);
            response.setStatus(HttpStatus.FORBIDDEN.value());
            response.setCharacterEncoding("UTF-8");
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.setPropertyNamingStrategy(new SnakeCaseStrategy());
            response.getWriter()
                .write(objectMapper.writeValueAsString(new ExceptionResponseBody(HttpStatus.FORBIDDEN.value(),
                    "Access is denied, you do not have permission to access this resource")));
        };
    }

    @Bean
    public OAuth2TokenCustomizer<JwtEncodingContext> oAuth2TokenCustomizer() {
        return new OAuth2TokenClaimsCustomizer(authUserRepository);
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> authUserRepository.findByEmail(username)
            .orElseThrow(() -> new EntityNotFoundException("User not found"));
    }

    @Bean
    public SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }

    @Bean
    public HttpSessionEventPublisher httpSessionEventPublisher() {
        return new HttpSessionEventPublisher();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

