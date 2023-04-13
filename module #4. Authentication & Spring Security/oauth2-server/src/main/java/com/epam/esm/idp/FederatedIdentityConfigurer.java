package com.epam.esm.idp;

import org.springframework.context.ApplicationContext;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.util.Assert;

import java.util.function.Consumer;

public final class FederatedIdentityConfigurer extends
    AbstractHttpConfigurer<FederatedIdentityConfigurer, HttpSecurity> {

    private Consumer<OAuth2User> oauth2UserHandler;

    public FederatedIdentityConfigurer oauth2UserHandler(Consumer<OAuth2User> oauth2UserHandler) {
        Assert.notNull(oauth2UserHandler, "oauth2UserHandler cannot be null");
        this.oauth2UserHandler = oauth2UserHandler;
        return this;
    }

    @Override
    public void init(HttpSecurity http) throws Exception {
        ApplicationContext applicationContext = http.getSharedObject(ApplicationContext.class);
        ClientRegistrationRepository clientRegistrationRepository =
            applicationContext.getBean(ClientRegistrationRepository.class);
        String loginPageUrl = "/login";
        FederatedIdentityAuthenticationEntryPoint authenticationEntryPoint =
            new FederatedIdentityAuthenticationEntryPoint(loginPageUrl, clientRegistrationRepository);

        FederatedIdentityAuthenticationSuccessHandler authenticationSuccessHandler =
            new FederatedIdentityAuthenticationSuccessHandler();
        if (this.oauth2UserHandler != null) {
            authenticationSuccessHandler.setOAuth2UserHandler(this.oauth2UserHandler);
        }

        http.exceptionHandling(
                exceptionHandling -> exceptionHandling.authenticationEntryPoint(authenticationEntryPoint))
            .oauth2Login(oauth2 -> oauth2.successHandler(authenticationSuccessHandler));
    }

}
