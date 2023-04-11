package com.epam.esm.service.impl;

import com.epam.esm.entity.AuthUserRole;
import com.epam.esm.exception.DuplicateKeyException;
import com.epam.esm.exception.EntityNotFoundException;
import com.epam.esm.repository.AuthUserRoleRepository;
import com.epam.esm.util.EntityModelFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class UserRoleServiceImplTest {

    private static final String ROLE_NAME = "ADMIN";
    private static final String NEW_ROLE_NAME = "MANAGER";

    @Mock
    private AuthUserRoleRepository authUserRoleRepository;

    @InjectMocks
    private UserRoleServiceImpl userRoleService;

    @Test
    void getByName_whenAuthUserRoleExist_shouldReturnAuthUserRole() {
        // given
        AuthUserRole expected = EntityModelFactory.createRole(ROLE_NAME);
        given(authUserRoleRepository.findByName(ROLE_NAME)).willReturn(Optional.of(expected));

        // when
        AuthUserRole result = userRoleService.getByName(ROLE_NAME);

        // then
        assertEquals(expected, result);
    }

    @Test
    void getByName_whenAuthUserRoleDoesNotExist_shouldThrowEntityNotFoundException() {
        // given
        given(authUserRoleRepository.findByName(ROLE_NAME)).willReturn(Optional.empty());

        // when/then
        assertThrows(EntityNotFoundException.class, () -> userRoleService.getByName(ROLE_NAME));
    }

    @Test
    void getAll_shouldReturnAllAuthUserRoles() {
        // given
        var allRoles = List.of(EntityModelFactory.createRole(ROLE_NAME), EntityModelFactory.createRole(NEW_ROLE_NAME));
        given(authUserRoleRepository.findAll()).willReturn(allRoles);

        // when
        List<AuthUserRole> result = userRoleService.getAll();

        // then
        assertEquals(allRoles, result);
    }

    @Test
    void create_whenAuthUserRoleDoesNotExist_shouldCreateNewAuthUserRole() {
        // given
        AuthUserRole newRole = EntityModelFactory.createRole(NEW_ROLE_NAME);
        given(authUserRoleRepository.findByName(NEW_ROLE_NAME)).willReturn(Optional.empty());
        given(authUserRoleRepository.save(newRole)).willReturn(newRole);

        // when
        AuthUserRole result = userRoleService.create(newRole);

        // then
        assertEquals(newRole, result);
    }

    @Test
    void create_whenAuthUserRoleExists_shouldThrowDuplicateKeyException() {
        // given
        AuthUserRole existingRole = EntityModelFactory.createRole(ROLE_NAME);
        given(authUserRoleRepository.findByName(ROLE_NAME)).willReturn(Optional.of(existingRole));

        // when/then
        assertThrows(DuplicateKeyException.class, () -> userRoleService.create(existingRole));
    }

    @Test
    void deleteByName_whenAuthUserRoleExists_shouldDeleteAuthUserRole() {
        // given
        AuthUserRole existingRole = EntityModelFactory.createRole(ROLE_NAME);
        given(authUserRoleRepository.findByName(ROLE_NAME)).willReturn(Optional.of(existingRole));

        // when
        userRoleService.deleteByName(ROLE_NAME);

        // then
        then(authUserRoleRepository).should().findByName(ROLE_NAME);
    }

    @Test
    void deleteByName_whenAuthUserRoleDoesNotExists_shouldThrowEntityNotFoundException() {
        // given
        given(authUserRoleRepository.findByName(ROLE_NAME)).willReturn(Optional.empty());

        // when/then
        assertThrows(EntityNotFoundException.class, () -> userRoleService.deleteByName(ROLE_NAME));
    }
}
