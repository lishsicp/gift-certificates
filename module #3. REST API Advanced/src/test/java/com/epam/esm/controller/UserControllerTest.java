package com.epam.esm.controller;

import com.epam.esm.assembler.UserAssembler;
import com.epam.esm.dto.UserDto;
import com.epam.esm.service.impl.UserServiceImpl;
import com.epam.esm.util.ModelFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserServiceImpl userService;

    @Mock
    private UserAssembler userAssembler;

    @InjectMocks
    private UserController userController;

    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    @DisplayName("GET /api/users - Success")
    void getAllUsers_shouldReturnTwo() throws Exception {
        UserDto userDto1 = ModelFactory.toUserDto(ModelFactory.createUser());
        UserDto userDto2 = ModelFactory.toUserDto(ModelFactory.createUser());
        List<UserDto> userDtos = List.of(userDto1, userDto2);
        Page<UserDto> users = new PageImpl<>(userDtos);

        given(userAssembler.toCollectionModel(any(), anyInt(), anyInt()))
                .willReturn(PagedModel.of(users.getContent(), new PagedModel.PageMetadata(0,0,0)));
        given(userService.getAll(anyInt(), anyInt())).willReturn(users);

        ResultActions resultActions = mockMvc.perform(get("/api/users")
                .contentType(MediaType.APPLICATION_JSON_VALUE));

        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(userDto1.getId()))
                .andExpect(jsonPath("$.content[0].name").value(userDto1.getName()))
                .andExpect(jsonPath("$.content[1].id").value(userDto2.getId()))
                .andExpect(jsonPath("$.content[1].name").value(userDto2.getName()));

        then(userService).should().getAll(anyInt(), anyInt());
        then(userAssembler).should().toCollectionModel(users, 1, 5);
    }

    @Test
    @DisplayName("GET /api/users/{id} - Success")
    void testGetUserById() throws Exception {
        UserDto userDto = ModelFactory.toUserDto(ModelFactory.createUser());

        given(userService.getById(anyLong())).willReturn(userDto);
        given(userAssembler.toModel(userDto)).willReturn(userDto);

        mockMvc.perform(get("/api/users/{id}", userDto.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userDto.getId()))
                .andExpect(jsonPath("$.name").value(userDto.getName()));

        then(userService).should().getById(anyLong());
        then(userAssembler).should().toModel(any(UserDto.class));
    }
}