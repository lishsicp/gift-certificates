package com.epam.esm.service.impl;

import com.epam.esm.dto.RegisteredClientDto;
import com.epam.esm.exception.DuplicateKeyException;
import com.epam.esm.exception.EntityNotFoundException;
import com.epam.esm.util.EntityModelFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class RegisteredClientServiceImplTest {

    @Mock
    private RegisteredClientRepository registeredClientRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Captor
    private ArgumentCaptor<RegisteredClient> registeredClientCaptor;

    @InjectMocks
    private RegisteredClientServiceImpl registeredClientService;

    @Test
    void create_whenRegisteredClientDoesNotExist_shouldCreateNewClient() {
        // given
        RegisteredClientDto registeredClientDto = EntityModelFactory.createRegisteredClientDto();
        RegisteredClient registeredClient = createRegisteredClient(registeredClientDto);
        given(registeredClientRepository.findByClientId(registeredClientDto.getClientId())).willReturn(null);
        given(passwordEncoder.encode(anyString())).willReturn("test_client_secret");

        // when
        registeredClientService.create(registeredClientDto);

        // then
        then(registeredClientRepository).should().findByClientId(registeredClientDto.getClientId());
        then(registeredClientRepository).should().save(registeredClientCaptor.capture());
        assertRegisteredClients(registeredClient, registeredClientCaptor.getValue());
    }

    @Test
    void create_whenRegisteredClientExists_shouldThrowDuplicateKeyException() {
        // given
        RegisteredClientDto registeredClientDto = EntityModelFactory.createRegisteredClientDto();
        RegisteredClient registeredClient = createRegisteredClient(registeredClientDto);
        given(registeredClientRepository.findByClientId(registeredClientDto.getClientId())).willReturn(
            registeredClient);

        // when
        assertThrows(DuplicateKeyException.class, () -> registeredClientService.create(registeredClientDto));

        // then
        then(registeredClientRepository).should().findByClientId(registeredClientDto.getClientId());
    }

    @Test
    void getByClientId_whenRegisteredClientExists_shouldReturnRegisteredClient() {
        // given
        String clientId = "testClientId";
        RegisteredClient registeredClient = createRegisteredClient(EntityModelFactory.createRegisteredClientDto());
        given(registeredClientRepository.findByClientId(clientId)).willReturn(registeredClient);
        
        // when
        RegisteredClient actualRegisteredClient = registeredClientService.getByClientId(clientId);

        // then
        assertRegisteredClients(registeredClient, actualRegisteredClient);
    }

    @Test
    void getByClientId_whenRegisteredClientDoesNotExists_shouldThrowException() {
        // given
        String clientId = "testClientId";

        // when/then
        assertThrows(EntityNotFoundException.class, () -> registeredClientService.getByClientId(clientId));
    }

    private RegisteredClient createRegisteredClient(RegisteredClientDto registeredClientDto) {
        return RegisteredClient.withId(UUID.randomUUID().toString())
            .clientIdIssuedAt(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant())
            .clientId(registeredClientDto.getClientId())
            .clientSecret(registeredClientDto.getClientSecret())
            .clientName(registeredClientDto.getClientName())
            .clientAuthenticationMethods(clientAuthenticationMethods -> clientAuthenticationMethods.addAll(
                registeredClientDto.getClientAuthenticationMethods()
                    .stream()
                    .map(ClientAuthenticationMethod::new)
                    .toList()))
            .authorizationGrantTypes(authorizationGrantTypes -> authorizationGrantTypes.addAll(
                registeredClientDto.getAuthorizationGrantTypes().stream().map(AuthorizationGrantType::new).toList()))
            .redirectUris(uris -> uris.addAll(registeredClientDto.getRedirectUris()))
            .scopes(scopes -> scopes.addAll(registeredClientDto.getScopes()))
            .clientSettings(ClientSettings.builder()
                .requireAuthorizationConsent(registeredClientDto.isRequireAuthorizationConsent())
                .requireProofKey(registeredClientDto.isRequireProofKey())
                .build())
            .tokenSettings(TokenSettings.builder()
                .refreshTokenTimeToLive(Duration.ofSeconds(registeredClientDto.getRefreshTokenTimeToLiveInSeconds()))
                .accessTokenTimeToLive(Duration.ofSeconds(registeredClientDto.getAccessTokenTimeToLiveInSeconds()))
                .build())
            .build();
    }

    private void assertRegisteredClients(RegisteredClient expectedRegisteredClient, RegisteredClient actualRegisteredClient) {
        assertNotNull(expectedRegisteredClient);
        assertEquals(actualRegisteredClient.getClientId(), expectedRegisteredClient.getClientId());
        assertEquals(actualRegisteredClient.getClientSecret(), expectedRegisteredClient.getClientSecret());
        assertEquals(actualRegisteredClient.getClientName(), expectedRegisteredClient.getClientName());
        assertEquals(actualRegisteredClient.getRedirectUris(), expectedRegisteredClient.getRedirectUris());
        assertEquals(actualRegisteredClient.getScopes(), expectedRegisteredClient.getScopes());
        assertEquals(actualRegisteredClient.getClientSettings().isRequireAuthorizationConsent(), expectedRegisteredClient.getClientSettings().isRequireAuthorizationConsent());
        assertEquals(actualRegisteredClient.getClientSettings().isRequireProofKey(), expectedRegisteredClient.getClientSettings().isRequireProofKey());
        assertEquals(actualRegisteredClient.getTokenSettings().getAccessTokenTimeToLive(), expectedRegisteredClient.getTokenSettings().getAccessTokenTimeToLive());
        assertEquals(actualRegisteredClient.getTokenSettings().getRefreshTokenTimeToLive(), expectedRegisteredClient.getTokenSettings().getRefreshTokenTimeToLive());
        assertEquals(actualRegisteredClient.getClientAuthenticationMethods().size(), expectedRegisteredClient.getClientAuthenticationMethods().size());
        assertEquals(actualRegisteredClient.getAuthorizationGrantTypes().size(), expectedRegisteredClient.getAuthorizationGrantTypes().size());
    }
}