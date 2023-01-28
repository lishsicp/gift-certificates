package com.epam.esm.dao;

import com.epam.esm.entity.Order;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface OrderDao extends CRUDDao<Order> {
    List<Order> findOrdersByUserId(Long id, Pageable pageable);
}
