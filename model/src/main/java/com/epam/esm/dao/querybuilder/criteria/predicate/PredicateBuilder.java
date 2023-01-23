package com.epam.esm.dao.querybuilder.criteria.predicate;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;

@FunctionalInterface
public interface PredicateBuilder {
    Predicate toPredicate(CriteriaBuilder cb, Expression<String> expression, String value);
}
