package com.epam.esm.assembler;

import com.epam.esm.controller.GiftCertificateController;
import com.epam.esm.dto.GiftCertificateDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


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
    public GiftCertificateDto toModel(GiftCertificateDto certificateDto) {
        certificateDto.add(linkTo(methodOn(GIFT_CERTIFICATE_CONTROLLER_CLASS).giftCertificateById(certificateDto.getId())).withSelfRel());
        certificateDto.getTags().forEach(tagModelAssembler::toModel);
        return certificateDto;
    }
}
