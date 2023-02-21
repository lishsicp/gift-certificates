package com.epam.esm.controller;

import com.epam.esm.assembler.OrderAssembler;
import com.epam.esm.service.dto.MakeOrderDto;
import com.epam.esm.service.dto.OrderDto;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Order;
import com.epam.esm.entity.User;
import com.epam.esm.service.OrderService;
import com.epam.esm.service.exception.PersistentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Min;
import java.util.List;


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

    private final OrderAssembler orderAssembler;


    @Autowired
    public OrderController(OrderService orderService, OrderAssembler orderAssembler) {
        this.orderService = orderService;
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
        OrderDto orderDto = orderService.getById(id);
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
    public PagedModel<OrderDto> ordersByUserId(
            @RequestParam(required = false, defaultValue = "1") @Min(value = 1, message = "40013") int page,
            @RequestParam(required = false, defaultValue = "5") @Min(value = 1, message = "40014") int size,
            @PathVariable @Min(value = 1, message = "40001") Long id) throws PersistentException {
        Page<OrderDto> orderDtos = orderService.getOrdersByUserId(id, page, size);
        return orderAssembler.toCollectionModel(orderDtos, page, size, id);
    }

    /**
     * Creates a new {@link Order} entity in database.
     *
     * @param orderDto must be valid according to {@link OrderDto} entity.
     * @return created {@link Order}. Response code 201.
     * @throws PersistentException if {@link Order} an error occurred.
     */
    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public OrderDto makeOrder(@RequestBody MakeOrderDto orderDto) throws PersistentException {
        OrderDto savedOrderDto = orderService.save(orderDto);
        return orderAssembler.toModel(savedOrderDto);
    }
}
