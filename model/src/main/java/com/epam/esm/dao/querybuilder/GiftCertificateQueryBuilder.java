package com.epam.esm.dao.querybuilder;

import com.epam.esm.entity.filter.SearchFilter;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class GiftCertificateQueryBuilder implements QueryBuilder {

    private StringBuilder query;

    public String buildFilteredSelectQuery(String selectQuery, SearchFilter searchFilter) {
        query = new StringBuilder(selectQuery);
        addPartParameter("tag.name", searchFilter.getTagName());
        addPartParameter("gc.name", searchFilter.getName());
        addPartParameter("gc.description", searchFilter.getDescription());
        addSortParameter(searchFilter.getSortBy(), searchFilter.getSortByType());
        return query.toString();
    }

    public String buildUpdateQuery(String query, Map<String, String> updateParams) {
        var updateQuery = new StringBuilder(query);
        String id = updateParams.get("id");
        updateParams.remove("id");
        for (Map.Entry<String, String> entry : updateParams.entrySet()) {
            updateQuery
                    .append(entry.getKey())
                    .append("=")
                    .append('\'').append(entry.getValue()).append('\'')
                    .append(", ");
        }
        updateQuery.deleteCharAt(updateQuery.length() - 2);
        updateQuery.append("WHERE id=").append(id);
        return updateQuery.toString();
    }

    public void addPartParameter(String column, String value) {
        if (StringUtils.isEmpty(value)) return;

        if (query.toString().contains("WHERE")) {
            query.append(" AND ");
        } else {
            query.append(" WHERE ");
        }
        query.append(column).append(" ilike ").append("'%").append(value).append("%'");
    }

    public void addSortParameter(String column, String value) {
        if (StringUtils.isEmpty(column)) return;
        query.append(" order by ").append("gc.").append(column.toLowerCase());
        query.append(" ").append(value == null ? "" : value);
    }
}
