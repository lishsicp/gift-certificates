package com.epam.esm.config;

import com.epam.esm.entity.AuthUser;
import com.epam.esm.jose.Jwks;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.authorization.JdbcOAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.JdbcOAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.client.JdbcRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;
import org.springframework.security.web.SecurityFilterChain;

import java.time.Duration;
import java.util.List;
import java.util.UUID;

@Configuration
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final CorsCustomizer corsCustomizer;

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity http) throws Exception {
        corsCustomizer.customize(http);

        OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http);
        http.getConfigurer(OAuth2AuthorizationServerConfigurer.class).oidc(Customizer.withDefaults());

        http.formLogin(Customizer.withDefaults());

        return http.build();
    }

    @Bean
    public OAuth2TokenCustomizer<JwtEncodingContext> auth2TokenCustomizer() {
        return context -> {
            var user = (AuthUser) context.getPrincipal().getPrincipal();

            context.getClaims().claim("name", user.getFirstname() + " " + user.getLastname());
            context.getClaims().claim("role", "ROLE_" + user.getRole().name());
        };
    }

    @Bean
    public JWKSource<SecurityContext> jwkSource() {
        RSAKey rsaKey = Jwks.generateRsa();
        JWKSet jwkSet = new JWKSet(rsaKey);
        return (jwkSelector, securityContext) -> jwkSelector.select(jwkSet);
    }

    @Bean
    public JwtDecoder jwtDecoder(JWKSource<SecurityContext> jwkSource) {
        return OAuth2AuthorizationServerConfiguration.jwtDecoder(jwkSource);
    }

    @Bean
    public AuthorizationServerSettings authorizationServerSettings() {
        return AuthorizationServerSettings.builder().issuer("http://oauth2-server:8082").build();
    }

    @Bean
    public RegisteredClientRepository registeredClientRepository(JdbcTemplate jdbcTemplate) {
        List<String> oauthScopes =
            List.of("tag.read", "tag.write", "user.write", "user.read", "certificate.read", "certificate.write",
                "order.read", "order.write", "user.order.read", "user.order.write");

        var tokenSettings = TokenSettings.builder()
            .accessTokenTimeToLive(Duration.ofMinutes(60))
            .refreshTokenTimeToLive(Duration.ofHours(24))
            .build();

        var clientSettings = ClientSettings.builder()
            .requireProofKey(true)
            .requireAuthorizationConsent(true)
            .build();

        RegisteredClient oauthClient = RegisteredClient.withId(UUID.randomUUID().toString())
            .clientId("client")
            .clientSecret("$2a$12$PQ1bsWmmCOpgCBtT6zVEmOVWuT8hh8QXiR8RvyS0MbexMKcD8WVca")
            .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
            .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_POST)
            .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
            .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
            .redirectUri("http://127.0.0.1:8080/authorized")
            .redirectUri("http://127.0.0.1/login/oauth2/code/google-idp")
            .scopes(set -> set.addAll(oauthScopes))
            .scope(OidcScopes.OPENID)
            .scope(OidcScopes.PROFILE)
            .tokenSettings(tokenSettings)
            .clientSettings(clientSettings)
            .build();

        JdbcRegisteredClientRepository jdbcRegisteredClientRepository =
            new JdbcRegisteredClientRepository(jdbcTemplate);

        jdbcRegisteredClientRepository.save(oauthClient);

        return jdbcRegisteredClientRepository;
    }

    @Bean
    public OAuth2AuthorizationConsentService authorizationConsentService(JdbcTemplate jdbcTemplate,
        RegisteredClientRepository registeredClientRepository) {
        return new JdbcOAuth2AuthorizationConsentService(jdbcTemplate, registeredClientRepository);
    }

    @Bean
    public OAuth2AuthorizationService authorizationService(JdbcTemplate jdbcTemplate,
        RegisteredClientRepository registeredClientRepository) {
        JdbcOAuth2AuthorizationService jdbcOAuth2AuthorizationService =
            new JdbcOAuth2AuthorizationService(jdbcTemplate, registeredClientRepository);
        jdbcOAuth2AuthorizationService.setAuthorizationRowMapper(new AuthUserRowMapper(registeredClientRepository));
        return jdbcOAuth2AuthorizationService;
    }

    private static class AuthUserRowMapper extends JdbcOAuth2AuthorizationService.OAuth2AuthorizationRowMapper {

        AuthUserRowMapper(RegisteredClientRepository registeredClientRepository) {
            super(registeredClientRepository);
            getObjectMapper().addMixIn(AuthUser.class, AuthUserMixin.class);
        }
    }

    @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE)
    private static class AuthUserMixin {

        @JsonCreator
        public AuthUserMixin(@JsonProperty("id") Long ignoredId, @JsonProperty("email") String ignoredEmail,
            @JsonProperty("password") String ignoredPassword, @JsonProperty("name") String ignoredName,
            @JsonProperty("role") String ignoredRole) {
        }
    }
}
