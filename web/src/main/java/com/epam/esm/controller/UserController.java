package com.epam.esm.controller;

import com.epam.esm.assembler.UserAssembler;
import com.epam.esm.dto.UserDto;
import com.epam.esm.dto.converter.UserConverter;
import com.epam.esm.entity.User;
import com.epam.esm.service.UserService;
import com.epam.esm.service.exception.PersistentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Min;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("api/users")
@Validated
public class UserController {

    private final UserService userService;
    private final UserConverter userConverter;
    private final UserAssembler userAssembler;

    @Autowired
    public UserController(UserService userService, UserConverter userConverter, UserAssembler userAssembler) {
        this.userService = userService;
        this.userConverter = userConverter;
        this.userAssembler = userAssembler;
    }

    @GetMapping()
    public CollectionModel<UserDto> allUsers(
            @RequestParam(required = false, defaultValue = "1") @Min(value = 1, message = "40013") int page,
            @RequestParam(required = false, defaultValue = "5") @Min(value = 1, message = "40014") int size) {
        List<UserDto> users = userService
                .getAll(page, size)
                .stream()
                .map(userConverter::toDto)
                .map(userAssembler::toModel)
                .collect(Collectors.toList());
        Link selfRel = linkTo(methodOn(this.getClass()).allUsers(page, size)).withSelfRel();
        return CollectionModel.of(users, selfRel);
    }

    @GetMapping("/{id}")
    public UserDto userById(@PathVariable @Min(value = 1, message = "40001") Long id) throws PersistentException {
        User user = userService.getById(id);
        return userAssembler.toModel(userConverter.toDto(user));
    }

}
