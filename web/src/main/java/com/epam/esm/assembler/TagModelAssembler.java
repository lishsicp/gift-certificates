package com.epam.esm.assembler;

import com.epam.esm.controller.TagController;
import com.epam.esm.service.dto.TagDto;
import org.springframework.data.domain.Page;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Component;


import java.util.LinkedList;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class TagModelAssembler implements ModelAssembler<TagDto> {

    private static final Class<TagController> TAG_CONTROLLER_CLASS = TagController.class;

    @Override
    public TagDto toModel(TagDto tagDto) {
        return tagDto.add(linkTo(methodOn(TAG_CONTROLLER_CLASS).tagById(tagDto.getId())).withSelfRel());
    }

    public PagedModel<TagDto> toCollectionModel(Page<TagDto> dtos, int page, int size) {
        List<TagDto> entityModels = new LinkedList<>();
        dtos.forEach(tagDto -> entityModels.add(toModel(tagDto)));
        Link selfRel = linkTo(methodOn(TAG_CONTROLLER_CLASS).allTags(page, size)).withSelfRel();
        PagedModel.PageMetadata metadata = new PagedModel.PageMetadata(dtos.getSize(), dtos.getNumber(), dtos.getTotalElements());
        return PagedModel.of(entityModels, metadata, selfRel);
    }
}
