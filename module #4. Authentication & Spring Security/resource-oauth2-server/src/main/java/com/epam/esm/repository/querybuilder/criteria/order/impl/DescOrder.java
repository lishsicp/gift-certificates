package com.epam.esm.repository.querybuilder.criteria.order.impl;

import com.epam.esm.repository.querybuilder.criteria.order.OrderBuilder;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Order;
import jakarta.persistence.criteria.Path;

public class DescOrder implements OrderBuilder {

    @Override
    public Order toOrder(CriteriaBuilder cb, Path<String> path) {
        return cb.desc(path);
    }
}
