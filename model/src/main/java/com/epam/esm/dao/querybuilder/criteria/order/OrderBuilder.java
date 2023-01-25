package com.epam.esm.dao.querybuilder.criteria.order;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Path;

public interface OrderBuilder {
    Order toOrder(CriteriaBuilder cb, Path<String> expression);
}
