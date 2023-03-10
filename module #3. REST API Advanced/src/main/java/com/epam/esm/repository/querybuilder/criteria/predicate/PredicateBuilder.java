package com.epam.esm.repository.querybuilder.criteria.predicate;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;

/**
 * A builder for constructing JPA {@link Predicate} objects
 * using a {@link CriteriaBuilder} and a {@link Predicate}.
 */
public interface PredicateBuilder {
    /**
     * Builds a {@link Predicate} that filters data based on the specified value and {@link Path}.
     *
     * @param cb    the JPA {@link CriteriaBuilder}
     * @param path  the JPA criteria {@link Path} to filter on
     * @param value the value to filter on
     * @return the constructed {@link Predicate}
     */
    Predicate toPredicate(CriteriaBuilder cb, Path<String> path, String value);
}
