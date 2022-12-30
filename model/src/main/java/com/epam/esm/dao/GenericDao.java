package com.epam.esm.dao;

import com.epam.esm.entity.Entity;
import org.springframework.jdbc.core.JdbcTemplate;

public abstract class GenericDao<T extends Entity> {

    protected JdbcTemplate jdbcTemplate;

    protected GenericDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
}
