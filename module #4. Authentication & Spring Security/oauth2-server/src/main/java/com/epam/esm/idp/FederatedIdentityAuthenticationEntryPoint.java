package com.epam.esm.idp;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestRedirectFilter;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

/**
 * This class is an implementation of AuthenticationEntryPoint interface for federated identity authentication.
 */
public final class FederatedIdentityAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    private final AuthenticationEntryPoint authenticationEntryPoint;

    private final ClientRegistrationRepository clientRegistrationRepository;

    public FederatedIdentityAuthenticationEntryPoint(String loginPageUrl,
        ClientRegistrationRepository clientRegistrationRepository) {
        this.authenticationEntryPoint = new LoginUrlAuthenticationEntryPoint(loginPageUrl);
        this.clientRegistrationRepository = clientRegistrationRepository;
    }

    /**
     * Redirects users to the identity provider's authentication page based on the 'idp' parameter value received with
     * the HTTP request, or directs users to the login page upon authentication failure.
     *
     * @param request                 the HTTP request
     * @param response                the HTTP response
     * @param authenticationException the authentication exception
     * @throws IOException      if an I/O related error occurs
     * @throws ServletException if a servlet related error occurs
     */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
        AuthenticationException authenticationException) throws IOException, ServletException {
        String idp = request.getParameter("idp");
        if (idp != null) {
            ClientRegistration clientRegistration = this.clientRegistrationRepository.findByRegistrationId(idp);
            if (clientRegistration != null) {
                String authorizationRequestUri =
                    OAuth2AuthorizationRequestRedirectFilter.DEFAULT_AUTHORIZATION_REQUEST_BASE_URI
                        + "/{registrationId}";
                String redirectUri = UriComponentsBuilder.fromHttpRequest(new ServletServerHttpRequest(request))
                    .replaceQuery(null)
                    .replacePath(authorizationRequestUri)
                    .buildAndExpand(clientRegistration.getRegistrationId())
                    .toUriString();
                this.redirectStrategy.sendRedirect(request, response, redirectUri);
                return;
            }
        }
        this.authenticationEntryPoint.commence(request, response, authenticationException);
    }
}
