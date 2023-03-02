package com.epam.esm.repository.querybuilder;

import org.springframework.util.MultiValueMap;

import javax.persistence.criteria.CriteriaQuery;

/**
 * The QueryBuilder interface provides methods for building JPA criteria queries for retrieving and counting entities
 * with filter and sort options based on a Spring MultiValueMap of query parameters.
 *
 * @param <T> the type of entity being queried
 */
public interface QueryBuilder<T> {
    /**
     * Builds a criteria query based on the given query parameters, with filter and sort options applied to the entity.
     *
     * @param params a MultiValueMap of query parameters to apply to the query
     * @return a CriteriaQuery object representing the constructed query
     */
    CriteriaQuery<T> buildQuery(MultiValueMap<String, String> params);

    /**
     * Builds a criteria query that counts the number of entities matching the given query parameters, with filter and
     * sort options applied.
     *
     * @param params a MultiValueMap of query parameters to apply to the query
     * @return a CriteriaQuery object representing the constructed query that counts the number of matching entities
     */
    CriteriaQuery<Long> countByQuery(MultiValueMap<String, String> params);
}
