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

import static org.junit.jupiter.api.Assertions.assertAll;
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
    void register_shouldCreateNewAuthUser_whenAuthUserDoesNotExists() {
        // given
        UserRegistrationDto userDto = EntityModelFactory.createUserRegistrationDto();
        AuthUserRole userRole = EntityModelFactory.createNewRole("USER");
        given(authUserRoleRepository.findByName("USER")).willReturn(Optional.of(userRole));
        given(passwordEncoder.encode(anyString())).willReturn(userDto.getPassword());
        given(authUserRepository.existsByEmail(userDto.getEmail())).willReturn(false);

        // when
        userRegistrationService.register(userDto);

        // then
        ArgumentCaptor<AuthUser> captor = ArgumentCaptor.forClass(AuthUser.class);
        then(authUserRepository).should().save(captor.capture());

        AuthUser registeredUser = captor.getValue();
        assertAll(() -> {
            assertEquals(userDto.getEmail(), registeredUser.getEmail());
            assertEquals(userDto.getFirstname(), registeredUser.getFirstname());
            assertEquals(userDto.getLastname(), registeredUser.getLastname());
            assertEquals(userDto.getPassword(), registeredUser.getPassword());
            assertEquals(userRole, registeredUser.getRole());
        });
    }

    @Test
    void register_shouldThrowDuplicateKeyException_whenAuthUserExists() {
        // given
        UserRegistrationDto userDto = EntityModelFactory.createUserRegistrationDto();
        given(authUserRepository.existsByEmail(userDto.getEmail())).willReturn(true);

        // when/then
        assertThrows(DuplicateKeyException.class, () -> userRegistrationService.register(userDto));
    }

    @Test
    void register_shouldThrowEntityNotFoundException_whenAuthUserRoleDoesNotExist() {
        // given
        UserRegistrationDto userDto = EntityModelFactory.createUserRegistrationDto();
        given(authUserRoleRepository.findByName("USER")).willReturn(Optional.empty());

        // when/then
        assertThrows(EntityNotFoundException.class, () -> userRegistrationService.register(userDto));
    }
}