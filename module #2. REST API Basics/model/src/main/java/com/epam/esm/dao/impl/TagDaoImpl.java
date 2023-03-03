package com.epam.esm.dao.impl;

import com.epam.esm.dao.GenericDao;
import com.epam.esm.dao.TagDao;
import com.epam.esm.dao.queries.TagSqlQueries;
import com.epam.esm.entity.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
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

    private final RowMapper<Tag> rowMapper;

    @Autowired
    public TagDaoImpl(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
        rowMapper = new BeanPropertyRowMapper<>(Tag.class);
    }

    @Override
    public List<Tag> findAll() {
        return jdbcTemplate.query(TagSqlQueries.GET_ALL, rowMapper);
    }

    @Override
    public Optional<Tag> findById(long id) {
        return jdbcTemplate.queryForStream(TagSqlQueries.GET_BY_ID, rowMapper, id)
                .findAny();
    }

    @Override
    public Optional<Tag> findByName(String name) {
        return jdbcTemplate.queryForStream(TagSqlQueries.GET_BY_NAME, rowMapper, name)
                .findAny();
    }

    @Override
    @Transactional
    public void delete(long id) {
        jdbcTemplate.update(TagSqlQueries.DELETE, id);
    }

    @Override
    @Transactional
    public Tag create(Tag tag) {
        KeyHolder holder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(TagSqlQueries.INSERT, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, tag.getName());
            return ps;
        }, holder);
        long id = getGeneratedId(holder);
        tag.setId(id);
        return tag;
    }

    @Override
    public List<Tag> findTagsForCertificate(long certificateId) {
        return jdbcTemplate.query(TagSqlQueries.GET_TAGS_FOR_CERTIFICATE, rowMapper, certificateId);
    }

    @Override
    public void assignTagToCertificate(long certificateId, long tagId) {
        jdbcTemplate.update(TagSqlQueries.ADD_TAG_TO_CERTIFICATE, certificateId, tagId);
    }

    @Override
    public void detachTagsFromCertificate(long certificateId) {
        jdbcTemplate.update(TagSqlQueries.DELETE_TAGS_FROM_CERTIFICATE, certificateId);
    }

}
