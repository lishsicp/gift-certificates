package com.epam.esm.controller;

import com.epam.esm.assembler.UserAssembler;
import com.epam.esm.dto.UserDto;
import com.epam.esm.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.hateoas.PagedModel;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Min;

/**
 * This class is used to implement controller logic for users.
 */
@RestController
@RequestMapping("api/users")
@Validated
public class UserController {

    private final UserService userService;
    private final UserAssembler userAssembler;

    public UserController(UserService userService, UserAssembler userAssembler) {
        this.userService = userService;
        this.userAssembler = userAssembler;
    }

    /**
     * Method used to get all users.
     * @param page The page number
     * @param size The page size
     * @return All the users
     */
    @GetMapping()
    public PagedModel<UserDto> allUsers(
            @RequestParam(required = false, defaultValue = "1") @Min(value = 1, message = "40013") int page,
            @RequestParam(required = false, defaultValue = "5") @Min(value = 1, message = "40014") int size) {
        Page<UserDto> users = userService.getAll(page, size);
        return userAssembler.toCollectionModel(users, page, size);
    }

    /**
     * Method used to get a user by id.
     * @param id The id of the user
     * @return The user with the given id
     */
    @GetMapping("/{id}")
    public UserDto userById(@PathVariable @Min(value = 1, message = "40001") long id) {
        UserDto userDto = userService.getById(id);
        return userAssembler.toModel(userDto);
    }
}