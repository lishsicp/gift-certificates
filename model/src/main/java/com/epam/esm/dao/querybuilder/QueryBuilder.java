package com.epam.esm.dao.querybuilder;

import com.epam.esm.entity.filter.SearchFilter;

import java.util.Map;

public interface QueryBuilder {
    String buildFilteredSelectQuery(String query, SearchFilter searchFilter);
    String buildUpdateQuery(String query, Map<String, String> updateParams);
}
