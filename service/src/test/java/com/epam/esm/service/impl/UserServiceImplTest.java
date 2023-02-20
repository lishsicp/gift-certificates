package com.epam.esm.service.impl;

import com.epam.esm.repository.UserRepository;
import com.epam.esm.entity.User;
import com.epam.esm.service.exception.PersistentException;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private static UserRepository userDao;

    @InjectMocks
    private UserServiceImpl service;

    private User USER_1;
    private User USER_2;
    private List<User> expectedUserList;

    @BeforeEach
    void setUp() {
        USER_1 = User.builder().id(1L).name("User1").build();
        USER_2 = User.builder().id(1L).name("User2").build();
        expectedUserList = Arrays.asList(USER_1, USER_2);
    }

    @Test
    void testGetAll_ShouldReturnTwoUsers() {
        int PAGE = 1;
        int SIZE = 5;
        when(userDao.findAll(any(Pageable.class))).thenReturn(new PageImpl<>(expectedUserList));
        Page<User> actual = service.getAll(PAGE, SIZE);
        assertEquals(expectedUserList, actual.getContent());
        verify(userDao).findAll(any(Pageable.class));
    }

    @Test
    void testFindById_ShouldThrowException() {
        Long userId = USER_2.getId();
        when(userDao.findById(userId)).thenReturn(Optional.empty());
        assertThrows(PersistentException.class, () -> service.getById(userId));
    }

    @Test
    void testFindById_ShouldReturnUser() {
        when(userDao.findById(any())).thenReturn(Optional.ofNullable(USER_1));
        User userById = service.getById(anyLong());
        assertEquals(USER_1, userById);
    }
}