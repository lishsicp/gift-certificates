package com.epam.esm.dao.impl;

import com.epam.esm.config.TestDaoConfig;
import com.epam.esm.dao.UserDao;
import com.epam.esm.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.ConfigDataApplicationContextInitializer;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;


import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = TestDaoConfig.class)
@ContextConfiguration(initializers = ConfigDataApplicationContextInitializer.class)
@ActiveProfiles("test")
class UserDaoImplTest {

    @Autowired
    private UserDao userDao;

    private static final Pageable PAGE_REQUEST = PageRequest.of(0, 5);
    private static final Long NON_EXISTED_USER_ID = 999L;

    private static final User USER_1 = User.builder().id(1L).name("User1").build();
    private static final User USER_2 = User.builder().id(2L).name("User2").build();
    private static final User USER_3 = User.builder().id(3L).name("User3").build();

    @Test
    void testFindAllShouldReturnAll() {
        List<User> users = userDao.findAll(PAGE_REQUEST);
        List<User> expected = Arrays.asList(USER_1,USER_2,USER_3);
        assertEquals(expected, users);
    }

    @Test
    void testFindByIdAShouldReturnOne() {
        Optional<User> user = userDao.findById(USER_1.getId());
        assertTrue(user.isPresent());
        assertEquals(USER_1, user.get());
    }

    @Test
    void testFindByIdAShouldBeEmpty() {
        Optional<User> user = userDao.findById(NON_EXISTED_USER_ID);
        assertTrue(user.isEmpty());
    }

    @Test
    void testDeleteThrowsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, () -> userDao.delete(USER_1.getId()));
    }

    @Test
    void testSaveThrowsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, () -> userDao.save(USER_1));
    }
}