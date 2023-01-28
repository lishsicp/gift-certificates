package com.epam.esm.assembler;

import com.epam.esm.controller.UserController;
import com.epam.esm.dto.UserDto;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class UserAssembler implements ModelAssembler<UserDto> {

    private static final Class<UserController> USER_CONTROLLER_CLASS = UserController.class;

    @Override
    public UserDto toModel(UserDto entity) {
        return entity.add(linkTo(methodOn(USER_CONTROLLER_CLASS).userById(entity.getId())).withSelfRel());
    }
}
