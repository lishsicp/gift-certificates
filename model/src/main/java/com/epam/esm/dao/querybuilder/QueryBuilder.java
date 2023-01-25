package com.epam.esm.dao.querybuilder;

import org.springframework.util.MultiValueMap;

import javax.persistence.criteria.CriteriaQuery;

public interface QueryBuilder<T> {
    CriteriaQuery<T> buildQuery(MultiValueMap<String, String> params);
}
