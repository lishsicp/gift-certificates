package com.epam.esm.controller;

import com.epam.esm.assembler.OrderAssembler;
import com.epam.esm.service.OrderService;
import com.epam.esm.service.dto.MakeOrderDto;
import com.epam.esm.service.dto.OrderDto;
import org.springframework.data.domain.Page;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.Min;


/**
 * This class is used to implement controller logic for orders.
 */
@RestController
@RequestMapping("api/orders")
@Validated
public class OrderController {

    private final OrderService orderService;
    private final OrderAssembler orderAssembler;

    public OrderController(OrderService orderService, OrderAssembler orderAssembler) {
        this.orderService = orderService;
        this.orderAssembler = orderAssembler;
    }

    /**
     * Method used to get order details by id.
     * @param id The id of the order
     * @return The order details
     */
    @GetMapping("/{id}")
    public OrderDto orderById(
            @PathVariable @Min(value = 1, message = "40001") long id) {
        OrderDto orderDto = orderService.getById(id);
        return orderAssembler.toModel(orderDto);
    }

    /**
     * Method used to get orders of a particular user.
     * @param page The page number
     * @param size The page size
     * @param id The id of the user
     * @return All the orders of the given user
     */
    @GetMapping("/users/{id}")
    public PagedModel<OrderDto> ordersByUserId(
            @RequestParam(required = false, defaultValue = "1") @Min(value = 1, message = "40013") int page,
            @RequestParam(required = false, defaultValue = "5") @Min(value = 1, message = "40014") int size,
            @PathVariable @Min(value = 1, message = "40001") long id) {
        Page<OrderDto> orderDtos = orderService.getOrdersByUserId(id, page, size);
        return orderAssembler.toCollectionModel(orderDtos, page, size, id);
    }

    /**
     * Method used to make a new order.
     * @param orderDto The details of the new order to make
     * @return The newly created order.
     */
    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public OrderDto makeOrder(@RequestBody @Valid MakeOrderDto orderDto) {
        OrderDto savedOrderDto = orderService.save(orderDto);
        return orderAssembler.toModel(savedOrderDto);
    }
}