package com.epam.esm.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class RegisteredClientDto implements Serializable {

    @Serial
	private static final long serialVersionUID = 5060408528228241329L;

    private static final String AUTHENTICATION_METHODS_REGEX = "client_secret_basic|client_secret_post|client_secret_jwt|private_key_jwt|none|";
    private static final String AUTHORIZATION_GRANT_REGEX = "authorization_code|refresh_token|client_credentials";
	private static final String SCOPE_REGEX = "\\D+.\\D+|\\D+:\\D+|\\D+";
    private static final String REDIRECT_URI_REGEX = "^(https?)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";

	private static final String SCOPES_MESSAGE = "Scopes can only have non-digit characters or/and be separated by '.' or ':'";
	private static final String AUTHENTICATION_METHODS_MESSAGE = "Can only be equal to 'client_secret_basic' or 'client_secret_post'";
	private static final String AUTHORIZATION_GRANT_MESSAGE = "Can only be equal to 'authorization_code', 'refresh_token' or 'client_credentials'";
	private static final String WORD_CHARACTERS_MESSAGE = "Only word characters";
	private static final String REDIRECT_URI_MESSAGE = "Doesn't look like URL";
    private static final String TOKEN_ERROR_MESSAGE = "Token time to live must be more then 300 seconds";

    @Pattern(regexp = "\\w+", message = WORD_CHARACTERS_MESSAGE)
    @NotBlank
    private String clientId;

    @Pattern(regexp = "\\w+", message = WORD_CHARACTERS_MESSAGE)
    @NotBlank
    private String clientSecret;

    @Pattern(regexp = "\\w+", message = WORD_CHARACTERS_MESSAGE)
    @NotBlank
    private String clientName;

    @NotEmpty
    private List<@Pattern(regexp = AUTHENTICATION_METHODS_REGEX, message = AUTHENTICATION_METHODS_MESSAGE) String>
        clientAuthenticationMethods;

    @NotEmpty
    private List<@Pattern(regexp = AUTHORIZATION_GRANT_REGEX, message = AUTHORIZATION_GRANT_MESSAGE) String>
        authorizationGrantTypes;

    @NotEmpty
    private List<@Pattern(regexp = REDIRECT_URI_REGEX, message = REDIRECT_URI_MESSAGE) String> redirectUris;

    @NotEmpty
    private List<@Pattern(regexp = SCOPE_REGEX, message = SCOPES_MESSAGE) String> scopes;

    @Min(value = 300, message = TOKEN_ERROR_MESSAGE)
    @Positive
    long accessTokenTimeToLiveInSeconds;

    @Min(value = 300, message = TOKEN_ERROR_MESSAGE)
    @Positive
    long refreshTokenTimeToLiveInSeconds;

    boolean requireProofKey;

    boolean requireAuthorizationConsent;
}