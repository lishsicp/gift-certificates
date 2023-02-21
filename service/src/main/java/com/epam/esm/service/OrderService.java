package com.epam.esm.service;

import com.epam.esm.service.dto.OrderDto;
import com.epam.esm.service.exception.PersistentException;
import org.springframework.data.domain.Page;


public interface OrderService extends CRDService<OrderDto> {
    Page<OrderDto> getOrdersByUserId(Long id, int page, int size) throws PersistentException;
}
