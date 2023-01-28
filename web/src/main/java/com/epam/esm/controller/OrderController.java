package com.epam.esm.controller;

import com.epam.esm.dto.MakeOrderDto;
import com.epam.esm.dto.OrderDto;
import com.epam.esm.dto.converter.OrderConverter;
import com.epam.esm.entity.Order;
import com.epam.esm.service.OrderService;
import com.epam.esm.service.exception.PersistentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.constraints.Min;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/orders")
@Validated
public class OrderController {

    private final OrderService orderService;

    private final OrderConverter orderConverter;

    @Autowired
    public OrderController(OrderService orderService, OrderConverter orderConverter) {
        this.orderService = orderService;
        this.orderConverter = orderConverter;
    }

    @GetMapping("/{id}")
    public OrderDto orderById(
            @PathVariable @Min(value = 1, message = "40001") Long id) throws PersistentException {
        Order order = orderService.getById(id);
        return orderConverter.toDto(order);
    }

    @GetMapping("/users/{id}")
    public List<OrderDto> ordersByUserId(
            @RequestParam(required = false, defaultValue = "1") @Min(value = 1, message = "40013") int page,
            @RequestParam(required = false, defaultValue = "5") @Min(value = 1, message = "40014") int size,
            @PathVariable @Min(value = 1, message = "40001") Long id) throws PersistentException {
        List<Order> orders = orderService.getOrdersByUserId(id, page, size);
        return orders.stream().map(orderConverter::toDto).collect(Collectors.toList());
    }

    @PostMapping()
    public ResponseEntity<Object> makeOrder(@RequestBody MakeOrderDto orderDto) throws PersistentException {
        Order saveOrder = orderService.save(orderConverter.toEntity(orderDto));
        URI locationUri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(saveOrder.getId())
                .toUri();
        return ResponseEntity.created(locationUri).body(orderConverter.toDto(saveOrder));
    }
}
