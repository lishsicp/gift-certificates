package com.epam.esm.service.impl;

import com.epam.esm.dto.RegisteredClientDto;
import com.epam.esm.dto.ScopesDto;
import com.epam.esm.exception.DuplicateKeyException;
import com.epam.esm.exception.EntityNotFoundException;
import com.epam.esm.service.RegisteredClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RegisteredClientServiceImpl implements RegisteredClientService {

    private static final String DO_NOT_EXIST = "Client with this '%s' do not exist";
    private static final String ALREADY_EXIST = "Client with this '%s' already exist";
    private final RegisteredClientRepository registeredClientRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void create(RegisteredClientDto registeredClientDto) {
        var registeredClient = registeredClientRepository.findByClientId(registeredClientDto.getClientId());
        if (registeredClient != null) {
            throw new DuplicateKeyException(String.format(ALREADY_EXIST, registeredClientDto.getClientId()));
        }
        RegisteredClient newClient = toRegisteredClient(registeredClientDto);
        registeredClientRepository.save(newClient);
    }

    @Override
    public RegisteredClientDto getByClientId(String clientId) {
        RegisteredClient registeredClient = fetchRegisteredClient(clientId);
        return toRegisteredClientDto(registeredClient);
    }

    @Override
    public RegisteredClientDto updateScopesByClientId(String clientId, ScopesDto scopes) {
        RegisteredClient registeredClient = fetchRegisteredClient(clientId);
        List<String> scopeList = List.of(scopes.getScopes().split("\\s"));
        RegisteredClient updatedClient = RegisteredClient.from(registeredClient).scopes(s -> {
            s.clear();
            s.addAll(scopeList);
        }).build();
        registeredClientRepository.save(updatedClient);
        return toRegisteredClientDto(updatedClient);
    }

    @Override
    public ScopesDto getScopesByClientId(String clientId) {
        RegisteredClient registeredClient = fetchRegisteredClient(clientId);
        return ScopesDto.builder()
            .scopes(String.join(" ", registeredClient.getScopes()))
            .build();
    }

    private RegisteredClient fetchRegisteredClient(String clientId) {
        var registeredClient = registeredClientRepository.findByClientId(clientId);
        if (registeredClient == null) {
            throw new EntityNotFoundException(String.format(DO_NOT_EXIST, clientId));
        }
        return registeredClient;
    }

    private RegisteredClient toRegisteredClient(RegisteredClientDto registeredClientDto) {
        return RegisteredClient.withId(UUID.randomUUID().toString())
            .clientIdIssuedAt(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant())
            .clientId(registeredClientDto.getClientId())
            .clientSecret(passwordEncoder.encode(registeredClientDto.getClientSecret()))
            .clientName(registeredClientDto.getClientName())
            .clientAuthenticationMethods(clientAuthenticationMethods -> clientAuthenticationMethods
                .addAll(registeredClientDto
                    .getClientAuthenticationMethods()
                    .stream()
                    .map(ClientAuthenticationMethod::new)
                    .toList())
            )
            .authorizationGrantTypes(authorizationGrantTypes -> authorizationGrantTypes
                .addAll(registeredClientDto
                    .getAuthorizationGrantTypes()
                    .stream()
                    .map(AuthorizationGrantType::new)
                    .toList())
            )
            .redirectUris(uris -> uris.addAll(registeredClientDto.getRedirectUris()))
            .scopes(scopes -> scopes.addAll(registeredClientDto.getScopes()))
            .clientSettings(ClientSettings.builder()
                .requireAuthorizationConsent(registeredClientDto.isRequireAuthorizationConsent())
                .requireProofKey(registeredClientDto.isRequireProofKey())
                .build()
            )
            .tokenSettings(TokenSettings.builder()
                .refreshTokenTimeToLive(
                    Duration.ofSeconds(registeredClientDto.getRefreshTokenTimeToLiveInSeconds()))
                .accessTokenTimeToLive(
                    Duration.ofSeconds(registeredClientDto.getAccessTokenTimeToLiveInSeconds()))
                .build()
            )
            .build();
    }

    private RegisteredClientDto toRegisteredClientDto(RegisteredClient registeredClient) {
        return RegisteredClientDto.builder()
            .clientId(registeredClient.getClientId())
            .clientSecret(registeredClient.getClientSecret())
            .clientName(registeredClient.getClientName())
            .clientAuthenticationMethods(registeredClient
                .getClientAuthenticationMethods()
                .stream().map(ClientAuthenticationMethod::getValue)
                .toList()
            )
            .authorizationGrantTypes(registeredClient
                .getAuthorizationGrantTypes()
                .stream()
                .map(AuthorizationGrantType::getValue)
                .toList())
            .redirectUris(registeredClient.getRedirectUris().stream().toList())
            .scopes(registeredClient.getScopes().stream().toList())
            .requireProofKey(registeredClient.getClientSettings().isRequireProofKey())
            .requireAuthorizationConsent(registeredClient.getClientSettings().isRequireAuthorizationConsent())
            .accessTokenTimeToLiveInSeconds(registeredClient.getTokenSettings().getAccessTokenTimeToLive().toSeconds())
            .refreshTokenTimeToLiveInSeconds(
                registeredClient.getTokenSettings().getRefreshTokenTimeToLive().toSeconds())
            .build();
    }
}
