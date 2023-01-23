package com.epam.esm.dao.querybuilder.criteria.order.impl;

import com.epam.esm.dao.querybuilder.criteria.order.OrderBuilder;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Order;

public class AscOrder implements OrderBuilder {
    @Override
    public Order toOrder(CriteriaBuilder cb, Expression<String> expression) {
        return cb.asc(expression);
    }
}
