package com.epam.esm.dao.impl;

import com.epam.esm.dao.GenericDao;
import com.epam.esm.dao.TagDao;
import com.epam.esm.dao.queries.TagQueries;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.DaoException;
import com.epam.esm.exception.ErrorCodes;
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
        return jdbcTemplate.query(TagQueries.GET_ALL, new BeanPropertyRowMapper<>(Tag.class));
    }

    @Override
    public Tag getById(long id) {
        return jdbcTemplate.query(TagQueries.GET_BY_ID, new BeanPropertyRowMapper<>(Tag.class), id)
                .stream()
                .findAny()
                .orElseThrow( ()-> new DaoException(ErrorCodes.TAG_NOT_FOUND, id));
    }

    @Override
    public Optional<Tag> getByName(String name) {
        return jdbcTemplate.query(TagQueries.GET_BY_NAME, new BeanPropertyRowMapper<>(Tag.class), name)
                .stream()
                .findAny();
    }

    @Override
    @Transactional
    public void remove(long id) {
        int updatedRows = jdbcTemplate.update(TagQueries.DELETE, id);
        if (updatedRows == 0) throw new DaoException(ErrorCodes.TAG_NOT_FOUND, id);
    }

    @Override
    @Transactional
    public Tag create(Tag tag) {
        KeyHolder holder = new GeneratedKeyHolder();
        int updatedFields = jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(TagQueries.INSERT, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, tag.getName());
            return ps;
        }, holder);
        if (updatedFields > 0) {
            tag.setId(((Number) holder.getKeys().get("id")).longValue());
            return tag;
        }
        throw new DaoException(ErrorCodes.SAVE_FAILURE);
    }

    @Override
    public List<Tag> getTagsForCertificate(long certificateId) {
        return jdbcTemplate.query(TagQueries.GET_TAGS_FOR_CERTIFICATE, new BeanPropertyRowMapper<>(Tag.class), certificateId);
    }

    @Override
    public void assignTagToCertificate(long certificateId, long tagId) {
        jdbcTemplate.update(TagQueries.ADD_TAG_TO_CERTIFICATE, certificateId, tagId);
    }

    @Override
    public void detachTagsFromCertificate(long certificateId) {
        jdbcTemplate.update(TagQueries.DELETE_TAGS_FROM_CERTIFICATE, certificateId);
    }

}
