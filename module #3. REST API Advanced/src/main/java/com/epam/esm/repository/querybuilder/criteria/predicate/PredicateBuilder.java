package com.epam.esm.repository.querybuilder.criteria.predicate;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;

/**
 * Defines the contract for a builder that creates a JPA {@link Predicate}
 * to filter data based on a value and a {@link Path}.
 */
public interface PredicateBuilder {
    /**
     * Builds a {@link Predicate} that filters data based on the specified value and {@link Path}.
     *
     * @param cb    the JPA criteria builder
     * @param path  the JPA criteria path to filter on
     * @param value the value to filter on
     * @return a {@link Predicate} that filters data based on the specified value and {@link Path}
     */
    Predicate toPredicate(CriteriaBuilder cb, Path<String> path, String value);
}
