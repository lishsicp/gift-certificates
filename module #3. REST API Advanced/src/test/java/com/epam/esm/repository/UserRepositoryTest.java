package com.epam.esm.repository;

import com.epam.esm.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;


import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Transactional
@ActiveProfiles("test")
@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository repository;

    private static final Pageable PAGE_REQUEST = PageRequest.of(0, 5);
    private static final Long NON_EXISTED_USER_ID = 999L;

    private static final User USER_1 = User.builder().id(1L).name("User1").build();
    private static final User USER_2 = User.builder().id(2L).name("User2").build();
    private static final User USER_3 = User.builder().id(3L).name("User3").build();

    @Test
    void findAllS_shouldReturnAll_whenValidPageRequest() {
        Page<User> users = repository.findAll(PAGE_REQUEST);
        List<User> expected = Arrays.asList(USER_1,USER_2,USER_3);
        assertEquals(expected, users.getContent());
    }

    @Test
    void findById_shouldReturnOne_whenExistingId() {
        Optional<User> user = repository.findById(USER_1.getId());
        assertTrue(user.isPresent());
        assertEquals(USER_1, user.get());
    }

    @Test
    void findById_shouldReturnEmptyOptional_whenNonexistentName() {
        Optional<User> user = repository.findById(NON_EXISTED_USER_ID);
        assertTrue(user.isEmpty());
    }
}