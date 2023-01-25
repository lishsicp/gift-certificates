package com.epam.esm.dao.querybuilder.criteria.predicate;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;

@FunctionalInterface
public interface PredicateBuilder {
    Predicate toPredicate(CriteriaBuilder cb, Path<String> path, String value);
}
