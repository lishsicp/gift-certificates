package com.epam.esm.service.impl;

import com.epam.esm.dto.UserRegistrationDto;
import com.epam.esm.entity.AuthUser;
import com.epam.esm.entity.AuthUserRole;
import com.epam.esm.exception.DuplicateKeyException;
import com.epam.esm.exception.EntityNotFoundException;
import com.epam.esm.repository.AuthUserRepository;
import com.epam.esm.repository.AuthUserRoleRepository;
import com.epam.esm.util.EntityModelFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class UserRegistrationServiceImplTest {

    @Mock
    private AuthUserRoleRepository authUserRoleRepository;

    @Mock
    private AuthUserRepository authUserRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserRegistrationServiceImpl userRegistrationService;

    @Test
    void register_whenAuthUserDoesNowExist_shouldCreateNewAuthUser() {
        // given
        UserRegistrationDto userRegistrationDto = EntityModelFactory.createUserRegistrationDto();
        AuthUserRole userRole = EntityModelFactory.createNewRole("USER");
        given(authUserRoleRepository.findByName("USER")).willReturn(Optional.of(userRole));
        given(passwordEncoder.encode(anyString())).willReturn(userRegistrationDto.getPassword());
        given(authUserRepository.existsByEmail(userRegistrationDto.getEmail())).willReturn(false);

        // when
        userRegistrationService.register(userRegistrationDto);

        // then
        ArgumentCaptor<AuthUser> captor = ArgumentCaptor.forClass(AuthUser.class);
        then(authUserRepository).should().save(captor.capture());

        AuthUser registeredUser = captor.getValue();
        assertEquals(registeredUser.getEmail(), userRegistrationDto.getEmail());
        assertEquals(registeredUser.getFirstname(), userRegistrationDto.getFirstname());
        assertEquals(registeredUser.getLastname(), userRegistrationDto.getLastname());
        assertEquals(registeredUser.getPassword(), userRegistrationDto.getPassword());
        assertEquals(registeredUser.getRole(), userRole);
    }

    @Test
    void register_whenAuthUserExist_shouldThrowDuplicateKeyException() {
        // given
        UserRegistrationDto userRegistrationDto = EntityModelFactory.createUserRegistrationDto();
        given(authUserRepository.existsByEmail(userRegistrationDto.getEmail())).willReturn(true);

        // when/then
        assertThrows(DuplicateKeyException.class, () -> userRegistrationService.register(userRegistrationDto));
    }

    @Test
    void register_whenAuthUserRoleDoesNotExist_shouldThrowEntityNotFoundException() {
        // given
        UserRegistrationDto userRegistrationDto = EntityModelFactory.createUserRegistrationDto();
        given(authUserRoleRepository.findByName("USER")).willReturn(Optional.empty());

        // when/then
        assertThrows(EntityNotFoundException.class, () -> userRegistrationService.register(userRegistrationDto));
    }
}