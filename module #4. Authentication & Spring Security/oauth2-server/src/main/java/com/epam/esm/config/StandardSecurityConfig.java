package com.epam.esm.config;

import com.epam.esm.exception.EntityNotFoundException;
import com.epam.esm.repository.AuthUserRepository;
import com.epam.esm.idp.FederatedIdentityConfigurer;
import com.epam.esm.idp.OAuth2TokenClaimsCustomizer;
import com.epam.esm.idp.UserRepositoryOAuth2UserHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
import org.springframework.security.web.session.HttpSessionEventPublisher;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class StandardSecurityConfig {

    private final CorsCustomizer corsCustomizer;
    private final AuthUserRepository authUserRepository;

    @Bean
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        corsCustomizer.customize(http);

        FederatedIdentityConfigurer federatedIdentityConfigurer = new FederatedIdentityConfigurer()
            .oauth2UserHandler(new UserRepositoryOAuth2UserHandler(authUserRepository));

        http
            .csrf(c -> c.ignoringRequestMatchers("/**"))
            .authorizeHttpRequests(
                authorize -> authorize
                    .requestMatchers("/oauth2/*", "/login", "/webjars/**").permitAll()
                    .anyRequest()
                    .authenticated())
            .formLogin(l -> l
                .loginPage("/login")
                .usernameParameter("email")
            );

        http.apply(federatedIdentityConfigurer);

        return http.build();
    }

    @Bean
    public OAuth2TokenCustomizer<JwtEncodingContext> idTokenCustomizer() {
        return new OAuth2TokenClaimsCustomizer(authUserRepository);
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> authUserRepository
            .findByEmail(username)
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

