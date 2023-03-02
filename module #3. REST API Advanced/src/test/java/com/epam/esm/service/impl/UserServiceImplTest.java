package com.epam.esm.service.impl;

import com.epam.esm.repository.UserRepository;
import com.epam.esm.entity.User;
import com.epam.esm.dto.UserDto;
import com.epam.esm.dto.converter.UserConverter;
import com.epam.esm.exception.PersistentException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
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

    private User USER_1;
    private User USER_2;
    private List<User> expectedUserList;

    private UserDto USER_DTO_1;
    private UserDto USER_DTO_2;
    private List<UserDto> expectedUserDtoList;

    @BeforeEach
    void setUp() {
        USER_1 = User.builder().id(1L).name("User1").build();
        USER_2 = User.builder().id(1L).name("User2").build();
        expectedUserList = Arrays.asList(USER_1, USER_2);

        USER_DTO_1 = UserDto.builder().id(1L).name("User1").build();
        USER_DTO_2 = UserDto.builder().id(1L).name("User2").build();
        expectedUserDtoList = Arrays.asList(USER_DTO_1, USER_DTO_2);
    }

    @Test
    void getAll_shouldReturnTwoUsers() {
        // Given
        int PAGE = 1;
        int SIZE = 5;
        given(repository.findAll(any(Pageable.class))).willReturn(new PageImpl<>(expectedUserList));
        given(userConverter.toDto(any())).willReturn(USER_DTO_1, USER_DTO_2);

        // When
        Page<UserDto> actual = service.getAll(PAGE, SIZE);

        // Then
        assertThat(actual.getContent()).isEqualTo(expectedUserDtoList);
        then(repository).should().findAll(any(Pageable.class));
    }

    @Test
    void getById_shouldThrowException_whenUserNonexistent() {
        Long userId = USER_2.getId();
        given(repository.findById(userId)).willReturn(Optional.empty());

        // Then
        assertThrows(PersistentException.class, () -> service.getById(userId));
        then(repository).should().findById(userId);
    }

    @Test
    void getById_shouldReturnUser() {
        // Given
        Long userId = USER_1.getId();
        given(repository.findById(userId)).willReturn(Optional.ofNullable(USER_1));
        given(userConverter.toDto(any())).willReturn(USER_DTO_1);

        // When
        UserDto userById = service.getById(userId);

        // Then
        assertThat(userById).isEqualTo(USER_DTO_1);
        then(repository).should().findById(userId);
    }
}