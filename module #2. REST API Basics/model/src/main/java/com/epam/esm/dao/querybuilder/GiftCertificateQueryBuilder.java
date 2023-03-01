package com.epam.esm.dao.querybuilder;

import com.epam.esm.entity.filter.SearchFilter;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class GiftCertificateQueryBuilder implements QueryBuilder {

    @Override
    public String buildFilteredSelectQuery(String selectQuery, SearchFilter searchFilter) {
        var query = new StringBuilder(selectQuery);
        addPartParameter(query, "tag.name", searchFilter.getTagName());
        addPartParameter(query, "gc.name", searchFilter.getName());
        addPartParameter(query, "gc.description", searchFilter.getDescription());
        addSortParameter(query, searchFilter.getSortBy(), searchFilter.getSortByType());
        return query.toString();
    }

    @Override
    public String buildUpdateQuery(String query, Map<String, String> updateParams) {
        Map<String, String> paramsCopy = new HashMap<>(updateParams);
        String id = paramsCopy.remove("id");
        var updateQuery = new StringBuilder(query);
        for (Map.Entry<String, String> entry : paramsCopy.entrySet()) {
            updateQuery.append(String.format(" %s = '%s',", entry.getKey(), entry.getValue()));
        }
        updateQuery.deleteCharAt(updateQuery.length() - 1);
        updateQuery.append(String.format(" WHERE id = %s", id));
        return updateQuery.toString();
    }

    private void addPartParameter(StringBuilder query, String column, String value) {
        if (StringUtils.isEmpty(value)) {
            return;
        }

        if (query.toString().contains("WHERE")) {
            query.append(" AND ");
        } else {
            query.append(" WHERE ");
        }
        query.append(String.format("%s ilike '%%%s%%'", column, value));
    }

    private void addSortParameter(StringBuilder query, String column, String value) {
        if (StringUtils.isEmpty(column)) {
            return;
        }
        query.append(String.format(" order by gc.%s %s",
                column.toLowerCase(), value == null ? "" : value));
    }
}

