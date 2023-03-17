package com.epam.esm.assembler;

import com.epam.esm.controller.OrderController;
import com.epam.esm.dto.OrderDto;
import org.springframework.data.domain.Page;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class OrderAssembler implements ModelAssembler<OrderDto> {

    private static final Class<OrderController> ORDER_CONTROLLER_CLASS = OrderController.class;

    private final GiftCertificateAssembler giftCertificateAssembler;
    private final UserAssembler userAssembler;

    public OrderAssembler(GiftCertificateAssembler giftCertificateAssembler, UserAssembler userAssembler) {
        this.giftCertificateAssembler = giftCertificateAssembler;
        this.userAssembler = userAssembler;
    }

    @Override
    public OrderDto toModel(OrderDto order) {
        giftCertificateAssembler.toModel(order.getGiftCertificate());
        userAssembler.toModel(order.getUser());
        return order.add(linkTo(methodOn(ORDER_CONTROLLER_CLASS).getOrderById(order.getId())).withSelfRel());
    }

    @Override
    public PagedModel<OrderDto> toCollectionModel(Page<OrderDto> dtos, Link selfRel) {
        List<OrderDto> entityModels = new LinkedList<>();
        dtos.forEach(order -> entityModels.add(toModel(order)));
        PagedModel.PageMetadata metadata =
            new PagedModel.PageMetadata(dtos.getSize(), dtos.getNumber(), dtos.getTotalElements());
        return PagedModel.of(entityModels, metadata, selfRel);
    }
}
