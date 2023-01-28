package com.epam.esm.service;

import com.epam.esm.entity.Order;
import com.epam.esm.service.exception.PersistentException;

import java.util.List;

public interface OrderService extends CRDService<Order> {
    List<Order> getOrdersByUserId(Long id, int page, int size) throws PersistentException;
}
