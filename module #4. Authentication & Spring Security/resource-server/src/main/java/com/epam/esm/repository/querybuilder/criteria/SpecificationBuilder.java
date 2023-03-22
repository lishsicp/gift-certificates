package com.epam.esm.repository.querybuilder.criteria;

import com.epam.esm.repository.querybuilder.criteria.order.OrderBuilder;
import com.epam.esm.repository.querybuilder.criteria.order.impl.AscOrder;
import com.epam.esm.repository.querybuilder.criteria.order.impl.DescOrder;
import com.epam.esm.repository.querybuilder.criteria.predicate.PredicateBuilder;
import com.epam.esm.repository.querybuilder.criteria.predicate.impl.LikePredicate;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Order;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;

import java.util.Map;

/**
 * Abstract class that defines common methods and properties for creating JPA specifications. It provides helper methods
 * for building predicates and orders for use in CriteriaQuery objects.
 */
public abstract class SpecificationBuilder {

    private static final String ASC = "asc";
    private static final String DESC = "desc";
    private static final String LIKE = "like";
    private final Map<String, PredicateBuilder> predicateBuilders = Map.of(
        LIKE, new LikePredicate()
    );

    private final Map<String, OrderBuilder> orderBuilders = Map.of(
        ASC, new AscOrder(),
        DESC, new DescOrder()
    );
    protected CriteriaBuilder criteriaBuilder;

    protected SpecificationBuilder(CriteriaBuilder criteriaBuilder) {
        this.criteriaBuilder = criteriaBuilder;
    }

    protected Predicate getLikePredicate(Path<String> path, String value) {
        PredicateBuilder predicateBuilder = predicateBuilders.get(LIKE);
        return predicateBuilder.toPredicate(this.criteriaBuilder, path, value);
    }

    protected Order getOrder(String order, Path<String> path) {
        OrderBuilder orderBuilder = orderBuilders.get(order);
        return orderBuilder.toOrder(this.criteriaBuilder, path);
    }
}