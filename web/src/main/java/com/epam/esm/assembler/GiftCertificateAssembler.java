package com.epam.esm.assembler;

import com.epam.esm.controller.GiftCertificateController;
import com.epam.esm.service.dto.GiftCertificateDto;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;


import java.util.LinkedList;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class GiftCertificateAssembler implements ModelAssembler<GiftCertificateDto> {

    private static final Class<GiftCertificateController> GIFT_CERTIFICATE_CONTROLLER_CLASS = GiftCertificateController.class;

    private final TagModelAssembler tagModelAssembler;

    @Autowired
    public GiftCertificateAssembler(TagModelAssembler tagModelAssembler) {
        this.tagModelAssembler = tagModelAssembler;
    }

    @Override
    public @NonNull GiftCertificateDto toModel(GiftCertificateDto certificateDto) {
        Link link = linkTo(methodOn(GIFT_CERTIFICATE_CONTROLLER_CLASS)
                .giftCertificateById(certificateDto.getId())).
                withSelfRel();
        certificateDto.add(link);
        certificateDto.getTags().forEach(tagModelAssembler::toModel);
        return certificateDto;
    }

    public @NonNull PagedModel<GiftCertificateDto> toCollectionModel(
            Page<GiftCertificateDto> certificates,
            int page, int size, MultiValueMap<String, String> filterParams) {
        List<GiftCertificateDto> entityModels = new LinkedList<>();
        certificates.forEach(certificate -> entityModels.add(toModel(certificate)));
        Link selfRel = linkTo(methodOn(GIFT_CERTIFICATE_CONTROLLER_CLASS)
                .findAllCertificatesWithParameters(page,size, filterParams)).withSelfRel();
        PagedModel.PageMetadata metadata = new PagedModel.PageMetadata(
                certificates.getSize(),
                certificates.getNumber(),
                certificates.getTotalElements()
        );
        return PagedModel.of(entityModels, metadata, selfRel);
    }
}
