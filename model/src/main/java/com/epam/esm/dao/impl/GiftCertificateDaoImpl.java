package com.epam.esm.dao.impl;

import com.epam.esm.dao.GenericDao;
import com.epam.esm.dao.GiftCertificateDao;
import com.epam.esm.dao.QueryBuilder;
import com.epam.esm.dao.queries.GiftCertificateQuery;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.filter.SearchFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;

@Repository
public class GiftCertificateDaoImpl extends GenericDao<GiftCertificate> implements GiftCertificateDao {


    @Autowired
    protected GiftCertificateDaoImpl(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    @Override
    public List<GiftCertificate> getAll() {
        return jdbcTemplate.query(GiftCertificateQuery.GET_ALL, new BeanPropertyRowMapper<>(GiftCertificate.class));
    }

    @Override
    public List<GiftCertificate> getAll(SearchFilter searchFilter) {
        String query = new QueryBuilder().buildFilteredSelectQuery(GiftCertificateQuery.GET_ALL_FILTERED, searchFilter);
        return jdbcTemplate.query(query, new BeanPropertyRowMapper<>(GiftCertificate.class));
    }

    @Override
    public Optional<GiftCertificate> getById(Long id) {
        return jdbcTemplate.query(GiftCertificateQuery.GET_BY_ID, new BeanPropertyRowMapper<>(GiftCertificate.class), id)
                .stream()
                .findAny();
    }

    @Override
    public void remove(Long id) {
        jdbcTemplate.update(GiftCertificateQuery.DELETE_BY_ID, id);
    }

    @Override
    public GiftCertificate create(GiftCertificate giftCertificate) {
        KeyHolder holder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(GiftCertificateQuery.INSERT, Statement.RETURN_GENERATED_KEYS);
            int index = 1;
            ps.setString(index++, giftCertificate.getName());
            ps.setString(index++, giftCertificate.getDescription());
            ps.setBigDecimal(index++, giftCertificate.getPrice());
            ps.setLong(index, giftCertificate.getDuration());
            return ps;
        }, holder);
        giftCertificate.setId(((Number) holder.getKeys().get("id")).longValue());
        giftCertificate.setCreateDate(((Timestamp) holder.getKeys().get("create_date")).toLocalDateTime());
        giftCertificate.setLastUpdateDate(((Timestamp) holder.getKeys().get("last_update_date")).toLocalDateTime());
        return giftCertificate;
    }

    @Override
    public void update(GiftCertificate giftCertificate) {
        jdbcTemplate.update(GiftCertificateQuery.UPDATE, giftCertificate.getName(), giftCertificate.getDescription(),
                giftCertificate.getPrice(), giftCertificate.getDuration(), LocalDateTime.now(), giftCertificate.getId());
    }

}
