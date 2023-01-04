package com.epam.esm.dao;

import com.epam.esm.entity.filter.SearchFilter;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

@Component
public class QueryBuilder {

    public String buildFilteredSelectQuery(String query, SearchFilter searchFilter) {
        StringBuilder selectQuery = new StringBuilder(query);
        if (!StringUtils.isEmpty(searchFilter.getTagName())) {
            selectQuery.append(addWhereLikeParameter(selectQuery, "tag.name", searchFilter.getTagName(), true));
        }
        if (!StringUtils.isEmpty(searchFilter.getName())) {
            selectQuery.append(addWhereLikeParameter(selectQuery, "gc.name", searchFilter.getName(), false));        }
        if (!StringUtils.isEmpty(searchFilter.getDescription())) {
            selectQuery.append(addWhereLikeParameter(selectQuery, "gc.description", searchFilter.getDescription(), false));
        }
        if (searchFilter.getOrderBy() != null) {
            selectQuery.append(" ORDER BY ").append("gc.").append(searchFilter.getOrderBy().toLowerCase());
            if (searchFilter.getOrderByType() != null) {
                selectQuery.append(" ").append(searchFilter.getOrderByType());
            }
        }
        return selectQuery.toString();
    }

    public String addWhereLikeParameter(StringBuilder query, String column, String value, boolean isStrictMatch) {
        StringBuilder result = new StringBuilder();
        if (query.toString().contains("WHERE")) {
            result.append(" AND ");
        } else {
            result.append(" WHERE ");
        }
        result.append(column).append(" ilike ").append("'%").append(value).append("%'");
        return isStrictMatch ? result.toString().replace("%", "") : result.toString();
    }
}
