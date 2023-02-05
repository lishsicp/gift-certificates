package com.epam.esm.controller;

import com.epam.esm.assembler.OrderAssembler;
import com.epam.esm.dto.MakeOrderDto;
import com.epam.esm.dto.OrderDto;
import com.epam.esm.dto.converter.OrderConverter;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Order;
import com.epam.esm.entity.User;
import com.epam.esm.service.OrderService;
import com.epam.esm.service.exception.PersistentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.constraints.Min;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * This class is an endpoint of the API which allows to perform CREATE and READ operations
 * with {@link Order} entities accessed through <i>api/orders</i>.
 * @author Lobur Yaroslav
 * @version 1.0
 */
@RestController
@RequestMapping("api/orders")
@Validated
public class OrderController {

    private final OrderService orderService;

    private final OrderConverter orderConverter;

    private final OrderAssembler orderAssembler;


    @Autowired
    public OrderController(OrderService orderService, OrderConverter orderConverter, OrderAssembler orderAssembler) {
        this.orderService = orderService;
        this.orderConverter = orderConverter;
        this.orderAssembler = orderAssembler;
    }

    /**
     * Gets a {@link Order} by its <code>id</code> from database.
     * @param id for {@link Order}
     * @return {@link Order} entity. Response code 200.
     * @throws PersistentException if {@link Order} is not found.
     */
    @GetMapping("/{id}")
    public OrderDto orderById(
            @PathVariable @Min(value = 1, message = "40001") Long id) throws PersistentException {
        Order order = orderService.getById(id);
        OrderDto orderDto = orderConverter.toDto(order);
        return orderAssembler.toModel(orderDto);
    }

    /**
     * Allows to get a list of {@link Order} for {@link User} by its id.
     * @param page         page number.
     * @param size         number of showed entities on page.
     * @param id for {@link User}
     * @return a {@link List} of found {@link GiftCertificate} entities with specified parameters.
     */
    @GetMapping("/users/{id}")
    public CollectionModel<OrderDto> ordersByUserId(
            @RequestParam(required = false, defaultValue = "1") @Min(value = 1, message = "40013") int page,
            @RequestParam(required = false, defaultValue = "5") @Min(value = 1, message = "40014") int size,
            @PathVariable @Min(value = 1, message = "40001") Long id) throws PersistentException {
        List<OrderDto> orders = orderService
                .getOrdersByUserId(id, page, size)
                .stream().map(orderConverter::toDto)
                .map(orderAssembler::toModel)
                .collect(Collectors.toList());
        Link selfRel = linkTo(methodOn(this.getClass()).ordersByUserId(page, size, id)).withSelfRel();
        return CollectionModel.of(orders, selfRel);
    }

    /**
     * Creates a new {@link Order} entity in database.
     *
     * @param orderDto must be valid according to {@link OrderDto} entity.
     * @return ResponseEntity with created {@link Order}. Response code 201.
     * @throws PersistentException if {@link Order} an error occurred.
     */
    @PostMapping()
    public ResponseEntity<Object> makeOrder(@RequestBody MakeOrderDto orderDto) throws PersistentException {
        Order savedOrder = orderService.save(orderConverter.toEntity(orderDto));
        URI locationUri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedOrder.getId())
                .toUri();
        OrderDto savedOrderDto = orderConverter.toDto(savedOrder);
        return ResponseEntity.created(locationUri).body(orderAssembler.toModel(savedOrderDto));
    }
}
