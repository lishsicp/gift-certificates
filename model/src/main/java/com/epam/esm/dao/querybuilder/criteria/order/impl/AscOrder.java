package com.epam.esm.dao.querybuilder.criteria.order.impl;

import com.epam.esm.dao.querybuilder.criteria.order.OrderBuilder;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Path;

public class AscOrder implements OrderBuilder {
    @Override
    public Order toOrder(CriteriaBuilder cb, Path<String> path) {
        return cb.asc(path);
    }
}
