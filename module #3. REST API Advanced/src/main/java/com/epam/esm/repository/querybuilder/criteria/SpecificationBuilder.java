package com.epam.esm.repository.querybuilder.criteria;

import com.epam.esm.repository.querybuilder.criteria.order.OrderBuilder;
import com.epam.esm.repository.querybuilder.criteria.order.impl.AscOrder;
import com.epam.esm.repository.querybuilder.criteria.order.impl.DescOrder;
import com.epam.esm.repository.querybuilder.criteria.predicate.PredicateBuilder;
import com.epam.esm.repository.querybuilder.criteria.predicate.impl.LikePredicate;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import java.util.AbstractMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Abstract class that defines common methods and properties for creating JPA specifications.
 * It provides helper methods for building predicates and orders for use in CriteriaQuery objects.
 */
public abstract class SpecificationBuilder {

    private static final String ASC = "asc";
    private static final String DESC = "desc";
    private static final String LIKE = "like";
    private final Map<String, PredicateBuilder> predicateBuilders = Stream.of(new AbstractMap.SimpleEntry<String, PredicateBuilder>(LIKE, new LikePredicate())).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    private final Map<String, OrderBuilder> orderBuilders = Stream.of(new AbstractMap.SimpleEntry<>(ASC, new AscOrder()), new AbstractMap.SimpleEntry<>(DESC, new DescOrder())).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
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