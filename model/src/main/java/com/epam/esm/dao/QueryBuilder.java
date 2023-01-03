package com.epam.esm.dao;

import com.epam.esm.entity.filter.SearchFilter;
import org.apache.commons.lang.StringUtils;


public class QueryBuilder {

    public String buildFilteredSelectQuery(String query, SearchFilter searchFilter) {
        StringBuilder selectQuery = new StringBuilder(query);
        if (!StringUtils.isEmpty(searchFilter.getTagName())) {
            containsWhere(selectQuery);
            selectQuery.append("tag.name").append(" = ").append("'").append(searchFilter.getTagName()).append("'");
        }
        if (!StringUtils.isEmpty(searchFilter.getName())) {
            containsWhere(selectQuery);
            selectQuery.append("gc.name").append(" ilike ").append("'%").append(searchFilter.getName()).append("%'");
        }
        if (!StringUtils.isEmpty(searchFilter.getDescription())) {
            containsWhere(selectQuery);
            selectQuery.append("gc.description").append(" ilike ").append("'%").append(searchFilter.getDescription()).append("%'");
        }
        if (searchFilter.getOrderBy() != null) {
            selectQuery.append(" ORDER BY ").append("gc.").append(searchFilter.getOrderBy().name().toLowerCase());
            if (searchFilter.getOrderByType() != null) {
                selectQuery.append(" ").append(searchFilter.getOrderByType().name());
            }
        }
        return selectQuery.toString();
    }

    public void containsWhere(StringBuilder query) {
        if (query.toString().contains("WHERE")) {
            query.append(" AND ");
        } else {
            query.append(" WHERE ");
        }
    }
}
