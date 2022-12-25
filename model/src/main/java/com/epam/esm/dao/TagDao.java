package com.epam.esm.dao;


import com.epam.esm.entity.Tag;
import com.epam.esm.exception.DBErrorCodes;
import com.epam.esm.exception.DBException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TagDao extends GenericDao<Tag> implements CRDDao<Tag> {

    @Autowired
    public TagDao(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    @Override
    public List<Tag> getAll() {
        return jdbcTemplate.query(TagQuery.GET_ALL, new BeanPropertyRowMapper<>(Tag.class));
    }

    @Override
    public Tag getById(Long id) throws DBException {
        return jdbcTemplate.query(TagQuery.GET_BY_ID, new Object[]{id} ,new BeanPropertyRowMapper<>(Tag.class))
                .stream()
                .findAny()
                .orElseThrow(() -> new DBException(DBErrorCodes.NOT_FOUND_BY_ID));
    }

    @Override
    public void remove(Tag tag) {
        jdbcTemplate.update(TagQuery.DELETE, tag.getId());
    }

    @Override
    public void create(Tag tag) {
        jdbcTemplate.update(TagQuery.INSERT, tag.getName());
    }
}
