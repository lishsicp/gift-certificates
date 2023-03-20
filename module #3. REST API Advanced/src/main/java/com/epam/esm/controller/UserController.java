package com.epam.esm.controller;

import com.epam.esm.assembler.UserAssembler;
import com.epam.esm.dto.UserDto;
import com.epam.esm.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Min;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * This class is used to implement controller logic for users.
 */
@RestController
@RequestMapping("api/users")
@Validated
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserAssembler userAssembler;

    /**
     * Method used to get all users.
     *
     * @param page The page number
     * @param size The page size
     * @return All the users
     */
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public PagedModel<UserDto> getAll(
        @RequestParam(required = false, defaultValue = "1") @Min(value = 1, message = "40013") int page,
        @RequestParam(required = false, defaultValue = "5") @Min(value = 1, message = "40014") int size) {
        Page<UserDto> users = userService.getAll(page, size);
        Link selfRel = linkTo(methodOn(this.getClass()).getAll(page, size)).withSelfRel();
        return userAssembler.toCollectionModel(users, selfRel);
    }

    /**
     * Method used to get a user by id.
     *
     * @param id The id of the user
     * @return The user with the given id
     */
    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public UserDto getById(@PathVariable @Min(value = 1, message = "40001") long id) {
        UserDto userDto = userService.getById(id);
        return userAssembler.toModel(userDto);
    }
}