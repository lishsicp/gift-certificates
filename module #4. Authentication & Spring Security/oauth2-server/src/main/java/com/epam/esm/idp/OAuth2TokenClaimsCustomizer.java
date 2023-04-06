package com.epam.esm.idp;

import com.epam.esm.repository.AuthUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.oidc.IdTokenClaimNames;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.endpoint.OidcParameterNames;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@RequiredArgsConstructor
public final class OAuth2TokenClaimsCustomizer implements OAuth2TokenCustomizer<JwtEncodingContext> {

	private final AuthUserRepository authUserRepository;

	private static final Set<String> ID_TOKEN_CLAIMS =
		Set.of(IdTokenClaimNames.ISS, IdTokenClaimNames.SUB, IdTokenClaimNames.AUD, IdTokenClaimNames.EXP,
			IdTokenClaimNames.IAT, IdTokenClaimNames.AUTH_TIME, IdTokenClaimNames.NONCE, IdTokenClaimNames.ACR,
			IdTokenClaimNames.AMR, IdTokenClaimNames.AZP, IdTokenClaimNames.AT_HASH, IdTokenClaimNames.C_HASH);

	@Override
	public void customize(JwtEncodingContext context) {
		authUserRepository.findByEmail(context.getPrincipal().getName()).ifPresent(user -> {
			context.getClaims()
				.claim("role", "ROLE_" + user.getRole().name());
			context.getClaims()
				.claim("name", user.getFirstname() + " " + user.getLastname());
		});

		if (OidcParameterNames.ID_TOKEN.equals(context.getTokenType().getValue())) {
			Map<String, Object> thirdPartyClaims = extractClaims(context.getPrincipal());
			context.getClaims().claims(existingClaims -> {
				existingClaims.keySet().forEach(thirdPartyClaims::remove);

				ID_TOKEN_CLAIMS.forEach(thirdPartyClaims::remove);

				existingClaims.putAll(thirdPartyClaims);
			});
		}
	}

	private Map<String, Object> extractClaims(Authentication principal) {
		Map<String, Object> claims = new HashMap<>();
		if (principal.getPrincipal() instanceof OidcUser oidcUser) {
			OidcIdToken idToken = oidcUser.getIdToken();
			claims.putAll(idToken.getClaims());
		} else if (principal.getPrincipal() instanceof OAuth2User oauth2User) {
			claims.putAll(oauth2User.getAttributes());
		}
		return claims;
	}
}
