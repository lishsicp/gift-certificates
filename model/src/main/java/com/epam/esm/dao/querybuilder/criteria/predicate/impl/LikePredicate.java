package com.epam.esm.dao.querybuilder.criteria.predicate.impl;

import com.epam.esm.dao.querybuilder.criteria.predicate.PredicateBuilder;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;

public class LikePredicate implements PredicateBuilder {
    @Override
    public Predicate toPredicate(CriteriaBuilder cb, Expression<String> expression, String value) {
        return cb.like(cb.lower(expression), "%" + value.toLowerCase() + "%");
    }
}
