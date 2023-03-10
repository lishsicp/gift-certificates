package com.epam.esm.assembler;

import com.epam.esm.controller.UserController;
import com.epam.esm.dto.UserDto;
import org.springframework.data.domain.Page;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class UserAssembler implements ModelAssembler<UserDto> {

    private static final Class<UserController> USER_CONTROLLER_CLASS = UserController.class;

    @Override
    public UserDto toModel(UserDto entity) {
        return entity.add(linkTo(methodOn(USER_CONTROLLER_CLASS).userById(entity.getId())).withSelfRel());
    }

    @Override
    public PagedModel<UserDto> toCollectionModel(Page<UserDto> dtos, Link selfRel) {
        List<UserDto> entityModels = new LinkedList<>();
        dtos.forEach(userDto -> entityModels.add(toModel(userDto)));
        PagedModel.PageMetadata metadata = new PagedModel
                .PageMetadata(dtos.getSize(), dtos.getNumber(), dtos.getTotalElements());
        return PagedModel.of(entityModels, metadata, selfRel);
    }
}
