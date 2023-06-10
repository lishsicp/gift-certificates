package com.epam.esm.service.impl;

import com.epam.esm.config.SpringConfig;
import com.epam.esm.dto.UserDto;
import com.epam.esm.dto.converter.UserConverter;
import com.epam.esm.exception.PersistentException;
import com.epam.esm.extension.PostgresExtension;
import com.epam.esm.repository.UserRepository;
import com.epam.esm.service.UserService;
import com.epam.esm.util.ModelFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.jdbc.TestDatabaseAutoConfiguration;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.test.context.ActiveProfiles;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Import(value = {UserServiceImpl.class, UserConverter.class, SpringConfig.class})
@ExtendWith(PostgresExtension.class)
@DataJpaTest(excludeAutoConfiguration = { TestDatabaseAutoConfiguration.class, SecurityAutoConfiguration.class })
@ActiveProfiles("test")
class UserServiceImplIT {

    @Autowired
    private UserRepository repository;

    @Autowired
    private UserService service;

    @Test
    void getAll_shouldReturnListOfOneUser() {
        int PAGE = 1;
        int SIZE = 5;
        var user = repository.save(ModelFactory.createNewUser());
        var userDto = ModelFactory.toUserDto(user);
        var userDtoList = Collections.singletonList(userDto);

        Page<UserDto> actual = service.getAll(PAGE, SIZE);

        assertEquals(userDtoList, actual.getContent());
    }

    @Test
    void getById_shouldThrowException_whenUserNonexistent() {
        var user = ModelFactory.createUser();
        long userId = user.getId();

        assertThrows(PersistentException.class, () -> service.getById(userId));
    }

    @Test
    void getById_shouldReturnUser() {
        var user = repository.save(ModelFactory.createNewUser());
        var userDto = ModelFactory.toUserDto(user);
        long userId = user.getId();

        UserDto userById = service.getById(userId);

        assertThat(userById).isEqualTo(userDto);
    }
}