package com.epam.esm.assembler;

import com.epam.esm.controller.TagController;
import com.epam.esm.dto.TagDto;
import org.springframework.stereotype.Component;


import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class TagModelAssembler implements ModelAssembler<TagDto> {

    private static final Class<TagController> TAG_CONTROLLER_CLASS = TagController.class;

    @Override
    public TagDto toModel(TagDto tagDto) {
        return tagDto.add(linkTo(methodOn(TAG_CONTROLLER_CLASS).tagById(tagDto.getId())).withSelfRel());
    }
}
