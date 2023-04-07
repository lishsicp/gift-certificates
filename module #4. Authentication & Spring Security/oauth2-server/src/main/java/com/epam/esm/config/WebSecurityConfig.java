package com.epam.esm.config;

import com.epam.esm.entity.AuthUser;
import com.epam.esm.idp.FederatedIdentityConfigurer;
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
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
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
import org.springframework.security.oauth2.server.authorization.oidc.authentication.OidcUserInfoAuthenticationContext;
import org.springframework.security.oauth2.server.authorization.oidc.authentication.OidcUserInfoAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.web.SecurityFilterChain;

import java.time.Duration;
import java.util.function.Function;

@Configuration(proxyBeanMethods = false)
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final CorsCustomizer corsCustomizer;

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity http) throws Exception {
        corsCustomizer.customize(http);

        OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http);
        http.getConfigurer(OAuth2AuthorizationServerConfigurer.class)
            .oidc(oidc -> oidc
                .userInfoEndpoint(userInfo -> userInfo
                    .userInfoMapper(userInfoMapper())));

        http
            .oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt) // enables OIDC 1.0 Userinfo endpoint
            .formLogin(Customizer.withDefaults());

        http.apply(new FederatedIdentityConfigurer());

        return http.build();
    }

    private Function<OidcUserInfoAuthenticationContext, OidcUserInfo> userInfoMapper() {
        return context -> {
            OidcUserInfoAuthenticationToken authentication = context.getAuthentication();
            JwtAuthenticationToken principal = (JwtAuthenticationToken) authentication.getPrincipal();
            return new OidcUserInfo(principal.getToken().getClaims());
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
        return AuthorizationServerSettings.builder()
            .issuer("http://oauth2-server.com:8082")
            .build();
    }

    @Bean
    public RegisteredClientRepository registeredClientRepository(JdbcTemplate jdbcTemplate) {

        JdbcRegisteredClientRepository jdbcRegisteredClientRepository =
            new JdbcRegisteredClientRepository(jdbcTemplate);

        RegisteredClient certificateClient = RegisteredClient.withId("e028af3e-7e40-49e1-8eb7-8be581ebd282")
            .clientId("gift-certificate-client")
            .clientSecret("$2a$12$PQ1bsWmmCOpgCBtT6zVEmOVWuT8hh8QXiR8RvyS0MbexMKcD8WVca")
            .clientName("gift-certificate-client")
            .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
            .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_POST)
            .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
            .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
            .redirectUri("http://127.0.0.1:8081/authorized")
            .redirectUri("http://127.0.0.1:8081/login/oauth2/code/gift-certificate-client-oidc")
            .scope(OidcScopes.OPENID)
            .scope(OidcScopes.PROFILE)
            .scope(OidcScopes.EMAIL)
            .scope("order.read")
            .scope("order.write")
            .scope("user.read")
            .scope("user.order.read")
            .scope("user.order.write")
            .clientSettings(ClientSettings.builder().requireAuthorizationConsent(true).build())
            .tokenSettings(TokenSettings.builder()
                .accessTokenTimeToLive(Duration.ofMinutes(60))
                .refreshTokenTimeToLive(Duration.ofDays(365))
                .build())
            .build();

        RegisteredClient byClientId = jdbcRegisteredClientRepository.findByClientId(certificateClient.getClientId());
        if (byClientId == null) {
            jdbcRegisteredClientRepository.save(certificateClient);
        }
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
        jdbcOAuth2AuthorizationService
            .setAuthorizationRowMapper(new AuthUserRowMapper(registeredClientRepository));
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
        public AuthUserMixin(
            @JsonProperty("id") Long ignoredId,
            @JsonProperty("email") String ignoredEmail,
            @JsonProperty("password") String ignoredPassword,
            @JsonProperty("firstname") String ignoredFirstname,
            @JsonProperty("lastname") String ignoredLastname,
            @JsonProperty("role") String ignoredRole) {
        }
    }
}
