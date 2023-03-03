package com.epam.esm.dao.querybuilder;

import com.epam.esm.entity.filter.SearchFilter;

import java.util.Map;

/**
 * The interface describes behavior for creating SQL queries.
 */
public interface QueryBuilder {

    /**
     * Builds a query with filter parameters.
     *
     * @param query query that contains DML command with table name.
     * @param searchFilter object for search parameters
     * @return a String SQL query.
     */
    String buildFilteredSelectQuery(String query, SearchFilter searchFilter);

    /**
     * Builds a query for update parameters.
     *
     * @param query        query that contains DML command with table name.
     * @param updateParams map of parameters to update
     * @return a String SQL query.
     */
    String buildUpdateQuery(String query, Map<String, String> updateParams);
}
