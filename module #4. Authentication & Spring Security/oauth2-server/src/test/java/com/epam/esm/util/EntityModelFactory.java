package com.epam.esm.util;

import com.epam.esm.dto.RegisteredClientDto;
import com.epam.esm.dto.UserRegistrationDto;
import com.epam.esm.entity.AuthUser;
import com.epam.esm.entity.AuthUserRole;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public final class EntityModelFactory {

    private static final AtomicLong counter = new AtomicLong();

    private EntityModelFactory() {
        throw new UnsupportedOperationException();
    }

    private static long generateId() {
        return counter.incrementAndGet();
    }

    public static AuthUserRole createRole(String roleName) {
        long id = generateId();
        return AuthUserRole.builder().id(id).name(roleName).build();
    }

    public static AuthUserRole createNewRole(String roleName) {
        return AuthUserRole.builder().name(roleName).build();
    }

    public static RegisteredClientDto createRegisteredClientDto() {
        return RegisteredClientDto.builder()
            .clientId("test_client_id")
            .clientSecret("test_client_secret")
            .clientName("Test Client")
            .requireAuthorizationConsent(false)
            .requireProofKey(false)
            .refreshTokenTimeToLiveInSeconds(3600)
            .accessTokenTimeToLiveInSeconds(600)
            .clientAuthenticationMethods(List.of("client_secret_basic", "client_secret_post"))
            .authorizationGrantTypes(List.of("authorization_code", "client_credentials"))
            .redirectUris(List.of("https://example.com/redirect", "https://example.com/redirect2"))
            .scopes(List.of("openid", "profile", "email"))
            .build();
    }

    public static UserRegistrationDto createUserRegistrationDto() {
        return UserRegistrationDto.builder()
            .email("test@example.com")
            .password("testPassword")
            .firstname("John")
            .lastname("Doe")
            .build();
    }

    public static AuthUser createNewAuthUser() {
        return AuthUser.builder()
            .email("test@example.com")
            .password("testPassword")
            .firstname("John")
            .lastname("Doe")
            .role(createNewRole("USER"))
            .build();
    }
}
