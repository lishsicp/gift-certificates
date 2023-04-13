package com.epam.esm.web;

import com.epam.esm.entity.AuthUserRole;
import com.epam.esm.exception.DuplicateKeyException;
import com.epam.esm.exception.EntityNotFoundException;
import com.epam.esm.extension.PostgresExtension;
import com.epam.esm.service.impl.UserRoleServiceImpl;
import com.epam.esm.util.EntityModelFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

import static com.epam.esm.util.EntityModelFactory.createNewRole;
import static com.epam.esm.util.JsonMapperUtil.asJson;
import static com.epam.esm.util.JsonMapperUtil.asObject;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Transactional
@ExtendWith(PostgresExtension.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
class UserRoleControllerIT {

    private static final String ROLE_NAME = "TEST";
    @Autowired
    private UserRoleServiceImpl userRoleService;
    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void create_whenRoleDoesNotExist_shouldReturnCreatedRole() throws Exception {
        // Given
        AuthUserRole role = createNewRole(ROLE_NAME);

        // When
        MvcResult result =
            mockMvc.perform(post("/oauth2/roles").content(asJson(role)).contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isCreated())
                .andReturn();

        // Then
        AuthUserRole createdRole = asObject(result, AuthUserRole.class);
        assertEquals(ROLE_NAME, createdRole.getName());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void create_whenRoleExist_shouldThrowDuplicateKeyException() throws Exception {
        // Given
        AuthUserRole role = EntityModelFactory.createNewRole(ROLE_NAME);
        userRoleService.create(role);

        // When
        ResultActions resultActions =
            mockMvc.perform(post("/oauth2/roles").content(asJson(role)).contentType(MediaType.APPLICATION_JSON_VALUE));

        // then
        resultActions.andExpect(res -> assertTrue(res.getResolvedException() instanceof DuplicateKeyException));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void getAll_shouldReturnAllRoles() throws Exception {
        // Given
        AuthUserRole role1 = createNewRole("TEST_1");
        AuthUserRole role2 = createNewRole("TEST_2");
        userRoleService.create(role1);
        userRoleService.create(role2);

        // When
        MvcResult result = mockMvc.perform(get("/oauth2/roles")).andExpect(status().isOk()).andReturn();

        // Then
        AuthUserRole[] roles = asObject(result, AuthUserRole[].class);
        assertThat(Arrays.asList(roles)).containsOnly(role1, role2);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void getByName_whenRoleExist_shouldReturnRole() throws Exception {
        // Given
        AuthUserRole role = createNewRole(ROLE_NAME);
        userRoleService.create(role);

        // When
        MvcResult result = mockMvc.perform(get("/oauth2/roles/" + ROLE_NAME)).andExpect(status().isOk()).andReturn();

        // Then
        AuthUserRole retrievedRole = asObject(result, AuthUserRole.class);
        assertEquals(role, retrievedRole);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void getByName_whenRoleDoesNotExist_shouldThrowEntityNotFoundException() throws Exception {
        // When
        ResultActions resultActions = mockMvc.perform(get("/oauth2/roles/" + ROLE_NAME));

        // Then
        resultActions.andExpect(res -> assertTrue(res.getResolvedException() instanceof EntityNotFoundException));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void deleteByName_whenRoleExist_shouldReturnNoContent() throws Exception {
        // Given
        AuthUserRole role = createNewRole(ROLE_NAME);
        userRoleService.create(role);

        // When
        ResultActions resultActions = mockMvc.perform(delete("/oauth2/roles/" + ROLE_NAME));

        // Then
        resultActions.andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void deleteByName_whenRoleDoesNotExist_shouldThrowEntityNotFoundException() throws Exception {
        // When
        ResultActions resultActions = mockMvc.perform(delete("/oauth2/roles/" + ROLE_NAME));

        // Then
        resultActions.andExpect(res -> assertTrue(res.getResolvedException() instanceof EntityNotFoundException));
    }
}