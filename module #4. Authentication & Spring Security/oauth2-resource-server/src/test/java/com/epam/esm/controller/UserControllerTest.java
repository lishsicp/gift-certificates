package com.epam.esm.controller;

import com.epam.esm.assembler.UserAssembler;
import com.epam.esm.dto.UserDto;
import com.epam.esm.service.impl.UserServiceImpl;
import com.epam.esm.util.ModelFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.security.core.authority.AuthorityUtils.createAuthorityList;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest(classes = UserController.class)
@AutoConfigureMockMvc
@AutoConfigureWebMvc
@EnableMethodSecurity
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserServiceImpl userService;

    @MockBean
    private UserAssembler userAssembler;

    @Test
    @DisplayName("GET /api/users - Success")
    void getAll_shouldReturnTwoUsers() throws Exception {
        // given
        UserDto userDto1 = ModelFactory.toUserDto(ModelFactory.createUser());
        UserDto userDto2 = ModelFactory.toUserDto(ModelFactory.createUser());
        List<UserDto> userDtos = List.of(userDto1, userDto2);
        Page<UserDto> users = new PageImpl<>(userDtos);

        given(userAssembler.toCollectionModel(any(), any(Link.class)))
            .willReturn(PagedModel.of(users.getContent(), new PagedModel.PageMetadata(0, 0, 0)));
        given(userService.getAll(anyInt(), anyInt())).willReturn(users);

        // when
        ResultActions resultActions = mockMvc.perform(get("/api/users")
            .with(jwt().authorities(createAuthorityList("ROLE_ADMIN", "SCOPE_user.read")))
            .contentType(MediaType.APPLICATION_JSON_VALUE));

        resultActions
            .andExpect(status().isOk())
            .andExpect(jsonPath("$._embedded.userDtoList[0].name").value(userDto1.getName()))
            .andExpect(jsonPath("$._embedded.userDtoList[0].email").value(userDto1.getEmail()))
            .andExpect(jsonPath("$._embedded.userDtoList[1].name").value(userDto2.getName()))
            .andExpect(jsonPath("$._embedded.userDtoList[1].email").value(userDto2.getEmail()));

        // then
        then(userService).should().getAll(anyInt(), anyInt());
        then(userAssembler).should().toCollectionModel(any(), any(Link.class));
    }

    @Test
    @DisplayName("GET /api/users/{id} - Success")
    void getById_shouldReturnUser() throws Exception {
        // given
        UserDto userDto = ModelFactory.toUserDto(ModelFactory.createUser());

        given(userService.getById(anyLong())).willReturn(userDto);
        given(userAssembler.toModel(userDto)).willReturn(userDto);

        // when
        mockMvc.perform(get("/api/users/{id}", userDto.getId())
            .with(jwt().authorities(createAuthorityList("ROLE_ADMIN", "SCOPE_user.read"))))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value(userDto.getName()))
            .andExpect(jsonPath("$.email").value(userDto.getEmail()));

        // then
        then(userService).should().getById(anyLong());
        then(userAssembler).should().toModel(any(UserDto.class));
    }
}