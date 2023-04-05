package com.epam.esm.idp;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import java.io.IOException;
import java.util.function.Consumer;

public final class FederatedIdentityAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

	private final AuthenticationSuccessHandler successHandler = new SavedRequestAwareAuthenticationSuccessHandler();

	private Consumer<OAuth2User> oauth2UserHandler = user -> {};

	private final Consumer<OidcUser> oidcUserHandler = user -> this.oauth2UserHandler.accept(user);

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
		if (authentication instanceof OAuth2AuthenticationToken) {
			if (authentication.getPrincipal() instanceof OidcUser oidcUser) {
				this.oidcUserHandler.accept(oidcUser);
			} else if (authentication.getPrincipal() instanceof OAuth2User oAuth2User) {
				this.oauth2UserHandler.accept(oAuth2User);
			}
		}
		this.successHandler.onAuthenticationSuccess(request, response, authentication);
	}

	public void setOAuth2UserHandler(Consumer<OAuth2User> oauth2UserHandler) {
		this.oauth2UserHandler = oauth2UserHandler;
	}

}
