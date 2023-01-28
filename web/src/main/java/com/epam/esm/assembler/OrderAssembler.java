package com.epam.esm.assembler;

import com.epam.esm.controller.OrderController;
import com.epam.esm.dto.OrderDto;
import org.springframework.stereotype.Component;

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
        return order.add(linkTo(methodOn(ORDER_CONTROLLER_CLASS).orderById(order.getId())).withSelfRel());
    }
}
