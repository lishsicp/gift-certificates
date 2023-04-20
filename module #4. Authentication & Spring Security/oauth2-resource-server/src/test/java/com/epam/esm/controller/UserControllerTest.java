package com.epam.esm.controller;

import com.epam.esm.assembler.UserAssembler;
import com.epam.esm.config.TestMethodSecurityConfiguration;
import com.epam.esm.config.UserIdPermissionEvaluator;
import com.epam.esm.dto.UserDto;
import com.epam.esm.service.impl.UserServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static com.epam.esm.util.ModelFactory.createUser;
import static com.epam.esm.util.ModelFactory.toUserDto;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;
import static org.springframework.security.core.authority.AuthorityUtils.createAuthorityList;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@Import(TestMethodSecurityConfiguration.class)
@SpringBootTest(classes = UserController.class)
@AutoConfigureMockMvc
@AutoConfigureWebMvc
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserServiceImpl userService;

    @MockBean
    private UserAssembler userAssembler;

    @MockBean
    private UserIdPermissionEvaluator userIdPermissionEvaluator;

    @Test
    @DisplayName("GET /api/users - should return 2 users when admin user gets users")
    void getAll_shouldReturnTwoUsers_whenAdminUserGetsUsers() throws Exception {
        // given
        UserDto userDto1 = toUserDto(createUser());
        UserDto userDto2 = toUserDto(createUser());
        List<UserDto> userDtos = List.of(userDto1, userDto2);
        Page<UserDto> users = new PageImpl<>(userDtos);

        given(userAssembler.toCollectionModel(any(), any(Link.class))).willReturn(
            PagedModel.of(users.getContent(), new PagedModel.PageMetadata(0, 0, 0)));
        given(userService.getAll(anyInt(), anyInt())).willReturn(users);

        // when
        ResultActions resultActions = mockMvc.perform(
            get("/api/users").with(jwt().authorities(createAuthorityList("ROLE_ADMIN", "SCOPE_user.read")))
                .contentType(MediaType.APPLICATION_JSON_VALUE));

        // then
        resultActions.andExpect(status().isOk())
            .andExpect(jsonPath("$._embedded.userDtoList[0].name").value(userDto1.getName()))
            .andExpect(jsonPath("$._embedded.userDtoList[0].email").value(userDto1.getEmail()))
            .andExpect(jsonPath("$._embedded.userDtoList[1].name").value(userDto2.getName()))
            .andExpect(jsonPath("$._embedded.userDtoList[1].email").value(userDto2.getEmail()));

        then(userService).should().getAll(anyInt(), anyInt());
        then(userAssembler).should().toCollectionModel(any(), any(Link.class));
    }

    @Test
    @DisplayName("GET /api/users - should respond with forbidden status code when user without user.read scope gets users")
    void getAll_shouldRespondWithForbiddenStatusCode_whenNoScope() throws Exception {
        // when
        ResultActions resultActions = mockMvc.perform(
            get("/api/users").with(jwt().authorities(createAuthorityList("ROLE_USER")))
                .contentType(MediaType.APPLICATION_JSON_VALUE));

        // then
        resultActions.andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("GET /api/users - should respond with unauthorized status code when user is not authenticated")
    void getAll_shouldRespondWithUnauthorizedStatusCode_whenUserIsNotAuthenticated() throws Exception {
        // when / then
        mockMvc.perform(get("/api/users")).andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("GET /api/users/{id} - should return user when admin user gets user")
    void getById_shouldReturnUser_whenAdminUserGetsUser() throws Exception {
        // given
        UserDto userDto = toUserDto(createUser());

        given(userService.getById(anyLong())).willReturn(userDto);
        given(userAssembler.toModel(userDto)).willReturn(userDto);

        // when
        ResultActions resultActions = mockMvc.perform(get("/api/users/{id}", userDto.getId()).with(
            jwt().authorities(createAuthorityList("ROLE_ADMIN"))));

        // then
        resultActions.andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value(userDto.getName()))
            .andExpect(jsonPath("$.email").value(userDto.getEmail()));

        then(userIdPermissionEvaluator).should(never()).hasPermission(any(), anyLong(), any(), any());
        then(userService).should().getById(anyLong());
        then(userAssembler).should().toModel(any(UserDto.class));
    }

    @Test
    @DisplayName("GET /api/users/{id} - should return user when user gets their own resource")
    void getById_shouldReturnUser_whenUserGetsTheirOwnResource() throws Exception {
        // given
        UserDto userDto = toUserDto(createUser());

        given(userService.getById(anyLong())).willReturn(userDto);
        given(userAssembler.toModel(userDto)).willReturn(userDto);
        given(userIdPermissionEvaluator.hasPermission(any(), anyLong(), any(), any())).willReturn(true);

        // when
        ResultActions resultActions = mockMvc.perform(get("/api/users/{id}", userDto.getId()).with(
            jwt().authorities(createAuthorityList("ROLE_USER", "SCOPE_user.read"))));

        // then
        resultActions.andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value(userDto.getName()))
            .andExpect(jsonPath("$.email").value(userDto.getEmail()));

        then(userIdPermissionEvaluator).should().hasPermission(any(), anyLong(), any(), any());
        then(userService).should().getById(anyLong());
        then(userAssembler).should().toModel(any(UserDto.class));
    }

    @Test
    @DisplayName("GET /api/users/{id} - should respond with forbidden status code when user gets not their own resource")
    void getById_shouldRespondWithForbiddenStatusCode_whenUserGetsNotTheirResource() throws Exception {
        // given
        long userId = 1;
        given(userIdPermissionEvaluator.hasPermission(any(), anyLong(), any(), any())).willReturn(false);

        // when / then
        mockMvc.perform(
                get("/api/users/{id}", userId).with(jwt().authorities(createAuthorityList("ROLE_USER", "SCOPE_user.read"))))
            .andExpect(status().isForbidden());

        then(userIdPermissionEvaluator).should().hasPermission(any(), anyLong(), any(), any());
        then(userService).should(never()).getById(anyLong());
        then(userAssembler).should(never()).toModel(any(UserDto.class));
    }

    @Test
    @DisplayName("GET /api/users/{id} - should respond with forbidden status code when user without user.read scope gets user")
    void getById_shouldRespondWithForbiddenStatusCode_whenNoScope() throws Exception {
        // given
        long userId = 1;

        // when / then
        mockMvc.perform(get("/api/users/{id}", userId).with(jwt().authorities(createAuthorityList("ROLE_USER"))))
            .andExpect(status().isForbidden());

        then(userService).should(never()).getById(anyLong());
        then(userAssembler).should(never()).toModel(any(UserDto.class));
    }


    @Test
    @DisplayName("GET /api/users/{id} - should respond with unauthorized status code when user is not authenticated")
    void getById_shouldRespondWithUnauthorizedStatusCode_whenUserIsNotAuthenticated() throws Exception {
        // given
        long userId = 1;

        // When / Then
        mockMvc.perform(get("/api/users/{id}", userId)).andExpect(status().isUnauthorized());
    }
}