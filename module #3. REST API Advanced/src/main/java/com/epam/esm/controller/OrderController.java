package com.epam.esm.controller;

import com.epam.esm.assembler.OrderAssembler;
import com.epam.esm.dto.MakeOrderDto;
import com.epam.esm.dto.OrderDto;
import com.epam.esm.entity.User;
import com.epam.esm.service.OrderService;
import org.springframework.data.domain.Page;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
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
 * This class is used to implement controller logic for {@link OrderDto orders}.
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
     * Method to get {@link OrderDto order} details by id.
     * @param id The id of the {@link OrderDto order}
     * @return The {@link OrderDto order} details
     */
    @GetMapping("/{id}")
    public OrderDto orderById(
            @PathVariable @Min(value = 1, message = "40001") long id) {
        OrderDto orderDto = orderService.getById(id);
        return orderAssembler.toModel(orderDto);
    }

    /**
     * Method to get all {@link OrderDto orders} with Pagination.
     *
     * @param page number of the page
     * @param size number of items in a page
     * @return a {@link PagedModel} which contains all {@link OrderDto Orders}
     */
    @GetMapping()
    public PagedModel<OrderDto> allOrders(
            @RequestParam(required = false, defaultValue = "1") @Min(value = 1, message = "40013") int page,
            @RequestParam(required = false, defaultValue = "5") @Min(value = 1, message = "40014") int size
    ) {
        Page<OrderDto> orderDtos = orderService.getAll(page, size);
        return orderAssembler.toCollectionModel(orderDtos, page, size);
    }


    /**
     * Method used to get {@link OrderDto orders} of a particular {@link User user}.
     * @param page The page number
     * @param size The page size
     * @param id The id of the {@link User user}
     * @return All the {@link OrderDto orders} of the given {@link User user}
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
     * Method used to make a new {@link OrderDto order}.
     * @param orderDto The details of the new {@link OrderDto order} to make
     * @return The newly created {@link OrderDto order}.
     */
    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public OrderDto makeOrder(@RequestBody @Valid MakeOrderDto orderDto) {
        OrderDto savedOrderDto = orderService.save(orderDto);
        return orderAssembler.toModel(savedOrderDto);
    }


    /**
     * Method used to delete an existing {@link OrderDto order} by its id.
     *
     * @param id The id of the {@link OrderDto order} to delete
     * @return A response indicating the completion of the delete operation
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteOrder(@PathVariable @Valid @Min(value = 1, message = "40001") long id) {
        orderService.delete(id);
        return ResponseEntity.noContent().build();
    }
}