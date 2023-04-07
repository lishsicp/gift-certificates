package com.epam.esm.repository.querybuilder.criteria.order;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Order;
import jakarta.persistence.criteria.Path;

/**
 * A builder for constructing JPA {@link Order} objects using a {@link CriteriaBuilder} and a {@link Path}.
 */
public interface OrderBuilder {

    /**
     * Builds an {@link Order} using the provided {@link CriteriaBuilder} and {@link Path}.
     *
     * @param cb         The {@link CriteriaBuilder} to use for constructing the order.
     * @param expression The {@link Path} to use for specifying the attribute to order by.
     * @return the constructed {@link Order}.
     */
    Order toOrder(CriteriaBuilder cb, Path<String> expression);
}
