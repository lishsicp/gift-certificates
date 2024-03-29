package com.epam.esm.repository.querybuilder.criteria.order.impl;

import com.epam.esm.repository.querybuilder.criteria.order.OrderBuilder;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Path;

public class DescOrder implements OrderBuilder {

    @Override
    public Order toOrder(CriteriaBuilder cb, Path<String> path) {
        return cb.desc(path);
    }
}
