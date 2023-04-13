package com.epam.esm.service.impl;

import com.epam.esm.dto.RegisteredClientDto;
import com.epam.esm.dto.ScopesDto;
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
        given(passwordEncoder.encode(anyString())).willReturn(registeredClient.getClientSecret());

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
        RegisteredClientDto registeredClientDto = EntityModelFactory.createRegisteredClientDto();
        RegisteredClient registeredClient = createRegisteredClient(registeredClientDto);
        given(registeredClientRepository.findByClientId(clientId)).willReturn(registeredClient);

        // when
        RegisteredClientDto actualRegisteredClient = registeredClientService.getByClientId(clientId);

        // then
        assertRegisteredClientsDto(registeredClientDto, actualRegisteredClient);
    }

    @Test
    void updateScopesByClientId_whenRegisteredClientExists_shouldReturnUpdatedRegisteredClient() {
        // given
        RegisteredClientDto registeredClientDto = EntityModelFactory.createRegisteredClientDto();
        RegisteredClient registeredClient = createRegisteredClient(registeredClientDto);
        String clientId = registeredClientDto.getClientId();
        String scopes = String.join(" ", registeredClientDto.getScopes());
        ScopesDto scopesDto = ScopesDto.builder().scopes(scopes).build();
        given(registeredClientRepository.findByClientId(clientId)).willReturn(registeredClient);

        // when
        RegisteredClientDto updatedClientDto = registeredClientService.updateScopesByClientId(clientId, scopesDto);

        // then
        then(registeredClientRepository).should().findByClientId(clientId);
        then(registeredClientRepository).should().save(registeredClient);

        assertNotNull(updatedClientDto);
        assertEquals(clientId, updatedClientDto.getClientId());
        assertEquals(scopes, String.join(" ", updatedClientDto.getScopes()));
    }

    @Test
    void updateScopesByClientId_whenRegisteredClientDoesNotExists_shouldThrowEntityNotFoundException() {
        // given
        String clientId = "testClientId";
        String scopes = "read write";
        ScopesDto scopesDto = ScopesDto.builder().scopes(scopes).build();
        given(registeredClientRepository.findByClientId(clientId)).willReturn(null);

        // when/then
        assertThrows(EntityNotFoundException.class,
            () -> registeredClientService.updateScopesByClientId(clientId, scopesDto));
        then(registeredClientRepository).should().findByClientId(clientId);
        then(registeredClientRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    void getScopesByClientId_whenRegisteredClientExists_shouldReturnScopes() {
        // given
        RegisteredClient registeredClient = createRegisteredClient(EntityModelFactory.createRegisteredClientDto());
        given(registeredClientRepository.findByClientId(registeredClient.getClientId())).willReturn(registeredClient);

        // when
        ScopesDto scopesDto = registeredClientService.getScopesByClientId(registeredClient.getClientId());

        // then
        then(registeredClientRepository).should().findByClientId(registeredClient.getClientId());

        assertNotNull(scopesDto);
        assertEquals(String.join(" ", registeredClient.getScopes()), scopesDto.getScopes());
    }

    @Test
    void getScopesByClientId_whenRegisteredClientDoesNotExists_shouldThrowEntityNotFoundException() {
        // given
        String clientId = "testClientId";
        given(registeredClientRepository.findByClientId(clientId)).willReturn(null);

        // when/then
        assertThrows(EntityNotFoundException.class, () -> registeredClientService.getScopesByClientId(clientId));
        then(registeredClientRepository).should().findByClientId(clientId);
        then(registeredClientRepository).shouldHaveNoMoreInteractions();
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

    private void assertRegisteredClientsDto(RegisteredClientDto expected, RegisteredClientDto actual) {
        assertNotNull(actual);
        assertEquals(expected.getClientId(), actual.getClientId());
        assertEquals(expected.getClientSecret(), actual.getClientSecret());
        assertEquals(expected.getClientName(), actual.getClientName());
        assertEquals(expected.getRedirectUris(), actual.getRedirectUris());
        assertEquals(expected.getScopes(), actual.getScopes());
        assertEquals(expected.isRequireAuthorizationConsent(), actual.isRequireAuthorizationConsent());
        assertEquals(expected.isRequireProofKey(), actual.isRequireProofKey());
        assertEquals(expected.getAccessTokenTimeToLiveInSeconds(), actual.getAccessTokenTimeToLiveInSeconds());
        assertEquals(expected.getRefreshTokenTimeToLiveInSeconds(), actual.getRefreshTokenTimeToLiveInSeconds());
        assertEquals(expected.getClientAuthenticationMethods().size(), actual.getClientAuthenticationMethods().size());
        assertEquals(expected.getAuthorizationGrantTypes().size(), actual.getAuthorizationGrantTypes().size());
    }

    private void assertRegisteredClients(RegisteredClient expected, RegisteredClient actual) {
        assertNotNull(actual);
        assertEquals(expected.getClientId(), actual.getClientId());
        assertEquals(expected.getClientSecret(), actual.getClientSecret());
        assertEquals(expected.getClientName(), actual.getClientName());
        assertEquals(expected.getRedirectUris(), actual.getRedirectUris());
        assertEquals(expected.getScopes(), actual.getScopes());
        assertEquals(expected.getClientSettings().isRequireAuthorizationConsent(),
            actual.getClientSettings().isRequireAuthorizationConsent());
        assertEquals(expected.getClientSettings().isRequireProofKey(), actual.getClientSettings().isRequireProofKey());
        assertEquals(expected.getTokenSettings().getAccessTokenTimeToLive(),
            actual.getTokenSettings().getAccessTokenTimeToLive());
        assertEquals(expected.getTokenSettings().getRefreshTokenTimeToLive(),
            actual.getTokenSettings().getRefreshTokenTimeToLive());
        assertEquals(expected.getClientAuthenticationMethods().size(), actual.getClientAuthenticationMethods().size());
        assertEquals(expected.getAuthorizationGrantTypes().size(), actual.getAuthorizationGrantTypes().size());
    }
}