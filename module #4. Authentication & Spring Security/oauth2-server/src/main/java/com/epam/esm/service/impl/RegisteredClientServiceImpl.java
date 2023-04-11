package com.epam.esm.service.impl;

import com.epam.esm.dto.RegisteredClientDto;
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
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RegisteredClientServiceImpl implements RegisteredClientService {

    private final RegisteredClientRepository registeredClientRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void create(RegisteredClientDto registeredClientDto) {
        var registeredClient = registeredClientRepository.findByClientId(registeredClientDto.getClientId());
        if (registeredClient != null) {
            throw new DuplicateKeyException("Registered Client with this 'client_id' already exist");
        }

        RegisteredClient newClient = toRegisteredClient(registeredClientDto);
        registeredClientRepository.save(newClient);
    }

    @Override
    public RegisteredClient getByClientId(String clientId) {
        var registeredClient = registeredClientRepository.findByClientId(clientId);
        if (registeredClient == null) {
            throw new EntityNotFoundException("Registered Client with this 'client_id' do not exist");
        }
        return registeredClientRepository.findByClientId(clientId);
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
}
