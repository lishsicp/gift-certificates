package com.epam.esm.assembler;

import com.epam.esm.controller.GiftCertificateController;
import com.epam.esm.dto.GiftCertificateDto;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class GiftCertificateAssembler implements ModelAssembler<GiftCertificateDto> {

    private static final Class<GiftCertificateController> GIFT_CERTIFICATE_CONTROLLER_CLASS =
        GiftCertificateController.class;

    private final TagAssembler tagAssembler;

    @Autowired
    public GiftCertificateAssembler(TagAssembler tagAssembler) {
        this.tagAssembler = tagAssembler;
    }

    @Override
    public @NonNull GiftCertificateDto toModel(GiftCertificateDto certificateDto) {
        Link link = linkTo(
            methodOn(GIFT_CERTIFICATE_CONTROLLER_CLASS).getById(certificateDto.getId())).withSelfRel();
        certificateDto.add(link);
        certificateDto.getTags().forEach(tagAssembler::toModel);
        return certificateDto;
    }

    @Override
    public PagedModel<GiftCertificateDto> toCollectionModel(Page<GiftCertificateDto> dtos, Link selfRel) {
        List<GiftCertificateDto> entityModels = new LinkedList<>();
        dtos.forEach(certificate -> entityModels.add(toModel(certificate)));
        PagedModel.PageMetadata metadata =
            new PagedModel.PageMetadata(dtos.getSize(), dtos.getNumber(), dtos.getTotalElements());
        return PagedModel.of(entityModels, metadata, selfRel);
    }
}
