package com.epam.esm.controller;

import com.epam.esm.assembler.UserAssembler;
import com.epam.esm.dto.UserDto;
import com.epam.esm.exception.ExceptionMessageI18n;
import com.epam.esm.service.impl.UserServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest({UserController.class, ExceptionMessageI18n.class})
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserServiceImpl userService;

    @MockBean
    private UserAssembler userAssembler;

    @Test
    @DisplayName("GET /api/users - Success")
    void getAllUsers_shouldReturnTwo() throws Exception {
        List<UserDto> userDtos = List.of(
                new UserDto(1L, "John Doe"),
                new UserDto(2L, "Jane Doe")
        );
        Page<UserDto> users = new PageImpl<>(userDtos);

        given(userAssembler.toCollectionModel(any(), anyInt(), anyInt()))
                .willReturn(PagedModel.of(users.getContent(), new PagedModel.PageMetadata(0,0,0)));
        given(userService.getAll(anyInt(), anyInt())).willReturn(users);

        ResultActions resultActions = mockMvc.perform(get("/api/users")
                .contentType(MediaType.APPLICATION_JSON_VALUE));

        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.userDtoList[0].id").value(1))
                .andExpect(jsonPath("$._embedded.userDtoList[0].name").value("John Doe"))
                .andExpect(jsonPath("$._embedded.userDtoList[1].id").value(2))
                .andExpect(jsonPath("$._embedded.userDtoList[1].name").value("Jane Doe"));

        then(userService).should(times(1)).getAll(anyInt(), anyInt());
        then(userAssembler).should(times(1)).toCollectionModel(users, 1, 5);
    }

    @Test
    @DisplayName("GET /api/users/{id} - Success")
    void testGetUserById() throws Exception {
        UserDto userDto = new UserDto(1L, "John Doe");

        given(userService.getById(anyLong())).willReturn(userDto);
        given(userAssembler.toModel(userDto)).willReturn(userDto);

        mockMvc.perform(get("/api/users/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("John Doe"));

        then(userService).should(times(1)).getById(anyLong());
        then(userAssembler).should(times(1)).toModel(userDto);
    }
}