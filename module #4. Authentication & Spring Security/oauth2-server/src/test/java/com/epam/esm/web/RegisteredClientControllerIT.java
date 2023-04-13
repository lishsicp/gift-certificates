package com.epam.esm.web;

import com.epam.esm.dto.RegisteredClientDto;
import com.epam.esm.dto.ScopesDto;
import com.epam.esm.exception.DuplicateKeyException;
import com.epam.esm.exception.EntityNotFoundException;
import com.epam.esm.extension.PostgresExtension;
import com.epam.esm.service.impl.RegisteredClientServiceImpl;
import com.epam.esm.util.EntityModelFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import static com.epam.esm.util.JsonMapperUtil.asJson;
import static com.epam.esm.util.JsonMapperUtil.asObject;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Transactional
@ExtendWith(PostgresExtension.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@EnableMethodSecurity
class RegisteredClientControllerIT {

    @Autowired
    private RegisteredClientServiceImpl registeredClientService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void getByClientId_whenRegisteredClientExists_shouldReturnRegisteredClient() throws Exception {
        // given
        RegisteredClientDto dto = EntityModelFactory.createRegisteredClientDto();
        registeredClientService.create(dto);

        // when
        MvcResult mvcResult = mockMvc.perform(get("/connect/register/{clientId}", dto.getClientId()))
            .andExpect(status().isOk())
            .andReturn();

        // then
        var result = asObject(mvcResult, RegisteredClientDto.class);
        assertRegisteredClientDtos(dto, result);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void getByClientId_whenRegisteredClientDoesNotExists_shouldThrowEntityNotFoundException() throws Exception {
        // given
        String clientId = "client_id";

        // when
        ResultActions resultActions = mockMvc.perform(get("/connect/register/{clientId}", clientId));

        // then
        resultActions.andExpect(result -> assertTrue(result.getResolvedException() instanceof EntityNotFoundException));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void create_whenRegisteredClientDoesNotExist_shouldCreateNewClient() throws Exception {
        // given
        RegisteredClientDto dto = EntityModelFactory.createRegisteredClientDto();

        // when
        MvcResult mvcResult = mockMvc.perform(
                post("/connect/register").contentType(MediaType.APPLICATION_JSON_VALUE).content(asJson(dto)))
            .andExpect(status().isCreated())
            .andReturn();

        // then
        var result = asObject(mvcResult, RegisteredClientDto.class);
        assertRegisteredClientDtos(dto, result);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void create_whenRegisteredClientExist_shouldThrowDuplicateKeyException() throws Exception {
        // given
        RegisteredClientDto dto = EntityModelFactory.createRegisteredClientDto();
        registeredClientService.create(dto);

        // when
        ResultActions resultActions = mockMvc.perform(
            post("/connect/register").contentType(MediaType.APPLICATION_JSON_VALUE).content(asJson(dto)));

        // then
        resultActions.andExpect(result -> assertTrue(result.getResolvedException() instanceof DuplicateKeyException));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void updateScopes_whenRegisteredClientExists_shouldReturnUpdatedRegisteredClient() throws Exception {
        // given
        RegisteredClientDto dto = EntityModelFactory.createRegisteredClientDto();
        registeredClientService.create(dto);

        ScopesDto scopesDto = ScopesDto.builder().scopes("read write").build();

        // when
        MvcResult mvcResult = mockMvc.perform(
            patch("/connect/register/{clientId}/scopes", dto.getClientId()).contentType(
                MediaType.APPLICATION_JSON_VALUE).content(asJson(scopesDto))).andExpect(status().isOk()).andReturn();

        // then
        var result = asObject(mvcResult, RegisteredClientDto.class);
        assertEquals(scopesDto.getScopes(), String.join(" ", result.getScopes()));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void updateScopes_whenRegisteredClientDoesNotExists_shouldThrowEntityNotFoundException() throws Exception {
        // given
        String clientId = "client_id";
        ScopesDto scopesDto = ScopesDto.builder().scopes("read write").build();

        // when
        ResultActions resultActions = mockMvc.perform(
            patch("/connect/register/{clientId}/scopes", clientId).contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(asJson(scopesDto)));

        // then
        resultActions.andExpect(result -> assertTrue(result.getResolvedException() instanceof EntityNotFoundException));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void getScopes_whenRegisteredClientExists_shouldReturnScopes() throws Exception {
        // given
        RegisteredClientDto dto = EntityModelFactory.createRegisteredClientDto();
        ScopesDto scopesDto = ScopesDto.builder().scopes(String.join(" ", dto.getScopes())).build();
        registeredClientService.create(dto);

        // when
        MvcResult mvcResult = mockMvc.perform(get("/connect/register/{clientId}/scopes", dto.getClientId()))
            .andExpect(status().isOk())
            .andReturn();

        // then
        var result = asObject(mvcResult, ScopesDto.class);
        assertEquals(scopesDto.getScopes(), String.join(" ", result.getScopes()));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void getScopes_whenRegisteredClientDoesNotExists_shouldThrowEntityNotFoundException() throws Exception {
        // given
        String clientId = "client_id";

        // when
        ResultActions resultActions = mockMvc.perform(get("/connect/register/{clientId}/scopes", clientId));

        // then
        resultActions.andExpect(result -> assertTrue(result.getResolvedException() instanceof EntityNotFoundException));
    }

    private void assertRegisteredClientDtos(RegisteredClientDto expected, RegisteredClientDto actual) {
        assertNotNull(actual);
        assertEquals(expected.getClientId(), actual.getClientId());
        assertEquals(expected.getClientName(), actual.getClientName());
        assertEquals(expected.getRedirectUris(), actual.getRedirectUris());
        assertEquals(expected.getScopes(), actual.getScopes());
        assertEquals(expected.isRequireAuthorizationConsent(), actual.isRequireAuthorizationConsent());
        assertEquals(expected.isRequireProofKey(), actual.isRequireProofKey());
        assertEquals(expected.getAccessTokenTimeToLiveInSeconds(), actual.getAccessTokenTimeToLiveInSeconds());
        assertEquals(expected.getRefreshTokenTimeToLiveInSeconds(), actual.getRefreshTokenTimeToLiveInSeconds());
        assertEquals(expected.getClientAuthenticationMethods().size(), actual.getClientAuthenticationMethods().size());
        assertEquals(expected.getAuthorizationGrantTypes().size(), actual.getAuthorizationGrantTypes().size());
    }
}