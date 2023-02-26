package com.epam.esm.dao.impl;

import com.epam.esm.dao.GenericDao;
import com.epam.esm.dao.GiftCertificateDao;
import com.epam.esm.dao.querybuilder.QueryBuilder;
import com.epam.esm.dao.queries.GiftCertificateSqlQueries;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class GiftCertificateDaoImpl extends GenericDao<GiftCertificate> implements GiftCertificateDao {

    private final QueryBuilder queryBuilder;
    private final BeanPropertyRowMapper<GiftCertificate> rowMapper;

    @Autowired
    protected GiftCertificateDaoImpl(JdbcTemplate jdbcTemplate, QueryBuilder queryBuilder) {
        super(jdbcTemplate);
        this.queryBuilder = queryBuilder;
        rowMapper = new BeanPropertyRowMapper<>(GiftCertificate.class);
    }

    @Override
    public List<GiftCertificate> findAll() {
        return jdbcTemplate.query(GiftCertificateSqlQueries.GET_ALL, rowMapper);
    }

    @Override
    public List<GiftCertificate> findAll(SearchFilter searchFilter) {
        String query = queryBuilder.buildFilteredSelectQuery(GiftCertificateSqlQueries.GET_ALL_FILTERED, searchFilter);
        return jdbcTemplate.query(query, rowMapper);
    }

    @Override
    public Optional<GiftCertificate> findByName(String name) {
        return jdbcTemplate
                .queryForStream(GiftCertificateSqlQueries.GET_BY_NAME, rowMapper, name)
                .findAny();
    }

    @Override
    public Optional<GiftCertificate> findById(long id) {
        return jdbcTemplate.queryForStream(GiftCertificateSqlQueries.GET_BY_ID, rowMapper, id)
                .findAny();
    }

    @Override
    public void delete(long id) {
        jdbcTemplate.update(GiftCertificateSqlQueries.DELETE_BY_ID, id);
    }

    @Override
    public GiftCertificate create(GiftCertificate giftCertificate) {
        KeyHolder holder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(GiftCertificateSqlQueries.INSERT, Statement.RETURN_GENERATED_KEYS);
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
        long id = getGeneratedId(holder);
        giftCertificate.setId(id);
        return giftCertificate;
    }

    @Override
    public void update(GiftCertificate giftCertificate) {
        Map<String, String> updateParams = getUpdateParams(giftCertificate);
        String updateQuery = queryBuilder.buildUpdateQuery(GiftCertificateSqlQueries.UPDATE, updateParams);
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

        if (giftCertificate.getDuration() != null) {
            fields.put("duration", String.valueOf(giftCertificate.getDuration()));
        }
        fields.put("last_update_date", giftCertificate.getLastUpdateDate().toString());

        return fields;
    }

}
