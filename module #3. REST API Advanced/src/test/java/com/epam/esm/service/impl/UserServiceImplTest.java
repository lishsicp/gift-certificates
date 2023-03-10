package com.epam.esm.service.impl;

import com.epam.esm.dto.UserDto;
import com.epam.esm.dto.converter.UserConverter;
import com.epam.esm.exception.PersistentException;
import com.epam.esm.repository.UserRepository;
import com.epam.esm.util.ModelFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private static UserRepository repository;

    @Mock
    private static UserConverter userConverter;

    @InjectMocks
    private UserServiceImpl service;

    @Test
    void getAll_shouldReturnTwoUsers() {
        // Given
        int PAGE = 1;
        int SIZE = 5;
        var user = ModelFactory.createUser();
        var userDto = ModelFactory.toUserDto(user);
        var userList = Collections.singletonList(user);
        var userDtoList = Collections.singletonList(userDto);
        given(repository.findAll(any(Pageable.class))).willReturn(new PageImpl<>(userList));
        given(userConverter.toDto(any())).willReturn(userDto);

        // When
        Page<UserDto> actual = service.getAll(PAGE, SIZE);

        // Then
        assertEquals(actual.getContent(), userDtoList);
        then(repository).should().findAll(any(Pageable.class));
    }

    @Test
    void getById_shouldThrowException_whenUserNonexistent() {
        var user = ModelFactory.createUser();
        long userId = user.getId();

        // Then
        assertThrows(PersistentException.class, () -> service.getById(userId));
        then(repository).should().findById(userId);
    }

    @Test
    void getById_shouldReturnUser() {
        // Given
        var user = ModelFactory.createUser();
        var userDto = ModelFactory.toUserDto(user);
        long userId = user.getId();
        given(repository.findById(userId)).willReturn(Optional.of(user));
        given(userConverter.toDto(any())).willReturn(userDto);

        // When
        UserDto userById = service.getById(userId);

        // Then
        assertThat(userById).isEqualTo(userDto);
        then(repository).should().findById(userId);
    }
}