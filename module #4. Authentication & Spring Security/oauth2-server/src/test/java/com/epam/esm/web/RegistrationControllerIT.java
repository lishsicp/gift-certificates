package com.epam.esm.web;

import com.epam.esm.dto.UserRegistrationDto;
import com.epam.esm.exception.DuplicateKeyException;
import com.epam.esm.exception.EntityNotFoundException;
import com.epam.esm.extension.PostgresExtension;
import com.epam.esm.service.impl.UserRegistrationServiceImpl;
import com.epam.esm.service.impl.UserRoleServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import static com.epam.esm.util.EntityModelFactory.createNewRole;
import static com.epam.esm.util.EntityModelFactory.createUserRegistrationDto;
import static com.epam.esm.util.JsonMapperUtil.asJson;
import static com.epam.esm.util.JsonMapperUtil.asObject;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@Transactional
@ExtendWith(PostgresExtension.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
class RegistrationControllerIT {

    @Autowired
    private UserRegistrationServiceImpl userRegistrationService;

    @Autowired
    private UserRoleServiceImpl userRoleService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void register_whenAuthUserDoesNotExist_shouldCreateNewAuthUser() throws Exception {
        // given
        UserRegistrationDto userRegistrationDto = createUserRegistrationDto();

        userRoleService.create(createNewRole("USER"));

        // when
        MvcResult result = mockMvc.perform(post("/oauth2/register")
                .content(asJson(userRegistrationDto))
                .contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(status().isCreated())
            .andReturn();

        // then
        UserRegistrationDto createdUserRegistrationDto = asObject(result, UserRegistrationDto.class);

        assertEquals(userRegistrationDto.getEmail(), createdUserRegistrationDto.getEmail());
        assertEquals(userRegistrationDto.getFirstname(), createdUserRegistrationDto.getFirstname());
        assertEquals(userRegistrationDto.getLastname(), createdUserRegistrationDto.getLastname());
    }

    @Test
    void register_whenAuthUserExist_shouldThrowDuplicateKeyException() throws Exception {
        // given
        UserRegistrationDto userRegistrationDto = createUserRegistrationDto();
        userRoleService.create(createNewRole("USER"));
        userRegistrationService.register(userRegistrationDto);

        // when
        ResultActions resultActions = mockMvc.perform(post("/oauth2/register")
            .content(asJson(userRegistrationDto))
            .contentType(MediaType.APPLICATION_JSON_VALUE));

        // then
        resultActions
            .andExpect(result -> assertTrue(result.getResolvedException() instanceof DuplicateKeyException));
    }

    @Test
    void register_whenAuthUserRoleDoesNotExist_shouldThrowEntityNotFoundException() throws Exception {
        // given
        UserRegistrationDto userRegistrationDto = createUserRegistrationDto();

        // when
        ResultActions resultActions = mockMvc.perform(post("/oauth2/register")
            .content(asJson(userRegistrationDto))
            .contentType(MediaType.APPLICATION_JSON_VALUE));

        // then
        resultActions
            .andExpect(result -> assertTrue(result.getResolvedException() instanceof EntityNotFoundException));
    }
}