package com.epam.esm.idp;

import com.epam.esm.entity.AuthUser;
import com.epam.esm.entity.AuthUserRole;
import com.epam.esm.repository.AuthUserRepository;
import com.epam.esm.repository.AuthUserRoleRepository;
import com.epam.esm.util.EntityModelFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;

@ExtendWith(MockitoExtension.class)
class UserRepositoryOAuth2UserHandlerTest {

    private static final String USER_EMAIL = "test@example.com";
    @Mock
    private AuthUserRepository userRepository;
    @Mock
    private AuthUserRoleRepository userRoleRepository;
    @Mock
    private OAuth2User user;
    @InjectMocks
    private UserRepositoryOAuth2UserHandler userHandler;
    @Captor
    private ArgumentCaptor<AuthUser> userCaptor;

    @Test
    void accept_shouldSaveNewUser_whenUserDoesNotExist() {
        // Given
        String userName = "Test User";
        String[] fullName = userName.split("\\s");
        AuthUserRole role = EntityModelFactory.createNewRole("USER");

        given(user.getName()).willReturn(USER_EMAIL);
        given(user.getAttribute("name")).willReturn(userName);
        given(user.getAttribute("email")).willReturn(USER_EMAIL);
        given(userRoleRepository.findByName("USER")).willReturn(Optional.of(role));

        given(userRepository.existsByEmail(USER_EMAIL)).willReturn(false);

        // When
        userHandler.accept(user);

        // Then
        then(userRepository).should().existsByEmail(USER_EMAIL);

        then(userRepository).should().save(userCaptor.capture());
        AuthUser savedUser = userCaptor.getValue();

        assertEquals(USER_EMAIL, savedUser.getEmail());
        assertEquals(fullName[0], savedUser.getFirstname());
        assertEquals(fullName[1], savedUser.getLastname());
        assertNotNull(savedUser.getPassword());
        assertEquals(role, savedUser.getRole());
    }

    @Test
    void accept_shouldNotSaveNewUser_whenUserExists() {
        // given
        given(user.getName()).willReturn(USER_EMAIL);
        given(userRepository.existsByEmail(anyString())).willReturn(true);

        // when
        userHandler.accept(user);

        // then
        then(userRepository).should(never()).save(any(AuthUser.class));
    }

    @Test
    void accept_shouldNotSaveNewUser_whenNameAttributeIsNull() {
        // given
        given(user.getName()).willReturn(USER_EMAIL);
        given(user.getAttribute("name")).willReturn(null);
        given(userRepository.existsByEmail(anyString())).willReturn(false);

        // when
        userHandler.accept(user);

        // then
        then(userRepository).should(never()).save(any(AuthUser.class));
    }
}

