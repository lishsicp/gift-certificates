package com.epam.esm.dao.querybuilder.criteria.predicate.impl;

import com.epam.esm.dao.querybuilder.criteria.predicate.PredicateBuilder;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;

public class LikePredicate implements PredicateBuilder {
    @Override
    public Predicate toPredicate(CriteriaBuilder cb, Path<String> path, String value) {
        return cb.like(cb.lower(path), "%" + value.toLowerCase() + "%");
    }
}
