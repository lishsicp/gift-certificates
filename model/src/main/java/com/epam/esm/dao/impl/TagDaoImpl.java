package com.epam.esm.dao.impl;

import com.epam.esm.dao.GenericDao;
import com.epam.esm.dao.TagDao;
import com.epam.esm.dao.TagQuery;
import com.epam.esm.entity.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

@Repository
public class TagDaoImpl extends GenericDao<Tag> implements TagDao {

    @Autowired
    public TagDaoImpl(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    @Override
    public List<Tag> getAll() {
        return jdbcTemplate.query(TagQuery.GET_ALL, new BeanPropertyRowMapper<>(Tag.class));
    }

    @Override
    public Optional<Tag> getById(Long id) {
        return jdbcTemplate.query(TagQuery.GET_BY_ID, new BeanPropertyRowMapper<>(Tag.class), id)
                .stream()
                .findAny();
    }

    @Override
    public Optional<Tag> getByName(String name) {
        return jdbcTemplate.query(TagQuery.GET_BY_NAME, new BeanPropertyRowMapper<>(Tag.class), name)
                .stream()
                .findAny();
    }

    @Override
    @Transactional
    public void remove(Long id) {
        jdbcTemplate.update(TagQuery.DELETE, id);
    }

    @Override
    @Transactional
    public Tag create(Tag tag) {
        KeyHolder holder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(TagQuery.INSERT, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, tag.getName());
            return ps;
        }, holder);

        tag.setId(((Number) holder.getKeys().get("id")).longValue());
        return tag;
    }

}
