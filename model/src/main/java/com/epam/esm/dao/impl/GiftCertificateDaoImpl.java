package com.epam.esm.dao.impl;

import com.epam.esm.dao.GenericDao;
import com.epam.esm.dao.GiftCertificateDao;
import com.epam.esm.dao.QueryBuilder;
import com.epam.esm.dao.queries.GiftCertificateQuery;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.filter.SearchFilter;
import com.epam.esm.exception.DaoException;
import com.epam.esm.exception.DaoExceptionErrorCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.*;

@Repository
public class GiftCertificateDaoImpl extends GenericDao<GiftCertificate> implements GiftCertificateDao {

    private final QueryBuilder queryBuilder;

    @Autowired
    protected GiftCertificateDaoImpl(JdbcTemplate jdbcTemplate, QueryBuilder queryBuilder) {
        super(jdbcTemplate);
        this.queryBuilder = queryBuilder;
    }

    @Override
    public List<GiftCertificate> getAll() {
        return jdbcTemplate.query(GiftCertificateQuery.GET_ALL, new BeanPropertyRowMapper<>(GiftCertificate.class));
    }

    @Override
    public List<GiftCertificate> getAll(SearchFilter searchFilter) {
        String query = queryBuilder.buildFilteredSelectQuery(GiftCertificateQuery.GET_ALL_FILTERED, searchFilter);
        return jdbcTemplate.query(query, new BeanPropertyRowMapper<>(GiftCertificate.class));
    }

    @Override
    public GiftCertificate getById(Long id) throws DaoException {
        return jdbcTemplate.query(GiftCertificateQuery.GET_BY_ID, new BeanPropertyRowMapper<>(GiftCertificate.class), id)
                .stream()
                .findAny()
                .orElseThrow(() -> new DaoException(DaoExceptionErrorCode.CERTIFICATE_NOT_FOUND));
    }

    @Override
    public void remove(Long id) throws DaoException {
        int updatedRows = jdbcTemplate.update(GiftCertificateQuery.DELETE_BY_ID, id);
        if (updatedRows == 0) throw new DaoException(DaoExceptionErrorCode.CERTIFICATE_NOT_FOUND);
    }

    @Override
    public GiftCertificate create(GiftCertificate giftCertificate) throws DaoException {
        KeyHolder holder = new GeneratedKeyHolder();
        int updatedFields = jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(GiftCertificateQuery.INSERT, Statement.RETURN_GENERATED_KEYS);
            int index = 1;
            ps.setString(index++, giftCertificate.getName());
            ps.setString(index++, giftCertificate.getDescription());
            ps.setBigDecimal(index++, giftCertificate.getPrice());
            ps.setLong(index++, giftCertificate.getDuration());
            Timestamp createdDate = Timestamp.valueOf(giftCertificate.getCreateDate());
            ps.setTimestamp(index++, createdDate);
            ps.setTimestamp(index, createdDate);
            return ps;
        }, holder);
        if (updatedFields > 0) {
            giftCertificate.setId(((Number) holder.getKeys().get("id")).longValue());
            return giftCertificate;
        }
        throw new DaoException(DaoExceptionErrorCode.CERTIFICATE_SAVE_FAILURE);
    }

    @Override
    public void update(GiftCertificate giftCertificate) {
        Map<String, String> updateParams = getUpdateParams(giftCertificate);
        String updateQuery = queryBuilder.buildUpdateQuery("UPDATE gift_certificate SET ", updateParams);
        jdbcTemplate.update(updateQuery);
    }

    private Map<String, String> getUpdateParams(GiftCertificate giftCertificate) {
        Map<String, String> fields = new HashMap<>();

        if (giftCertificate.getId() != 0) {
            fields.put("id", String.valueOf(giftCertificate.getId()));
        }

        if (giftCertificate.getName() != null) {
            fields.put("name", giftCertificate.getName());
        }

        if (giftCertificate.getDescription() != null) {
            fields.put("description", giftCertificate.getDescription());
        }

        if (giftCertificate.getPrice() != null) {
            fields.put("price", giftCertificate.getPrice().toString());
        }

        if (giftCertificate.getDuration() != 0) {
            fields.put("duration", String.valueOf(giftCertificate.getDuration()));
        }
        fields.put("last_update_date", giftCertificate.getLastUpdateDate().toString());

        return fields;
    }

}
