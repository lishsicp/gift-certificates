package com.epam.esm.dao.impl;

import com.epam.esm.dao.GenericDao;
import com.epam.esm.dao.GiftCertificateDao;
import com.epam.esm.dao.querybuilder.GiftCertificateQueryBuilder;
import com.epam.esm.dao.querybuilder.QueryBuilder;
import com.epam.esm.entity.GiftCertificate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MultiValueMap;

import javax.persistence.criteria.CriteriaQuery;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class GiftCertificateDaoImpl extends GenericDao<GiftCertificate> implements GiftCertificateDao {

    @Autowired
    protected GiftCertificateDaoImpl() {
        super(GiftCertificate.class);
    }

    @Override
    @Transactional
    public GiftCertificate update(GiftCertificate e) {
        entityManager.merge(e);
        return e;
    }

    @Override
    public List<GiftCertificate> findAllWithFilter(Pageable page, MultiValueMap<String, String> params) {
        QueryBuilder<GiftCertificate> queryBuilder = new GiftCertificateQueryBuilder(entityManager.getCriteriaBuilder());
        CriteriaQuery<GiftCertificate> criteriaQuery = queryBuilder.buildQuery(params);
        return entityManager
                .createQuery(criteriaQuery)
                .setFirstResult((int) page.getOffset())
                .setMaxResults(page.getPageSize())
                .getResultList()
                .stream()
                .distinct()
                .collect(Collectors.toList());
    }

    @Override
    public Optional<GiftCertificate> findByName(String name) {
        return entityManager
                .createQuery("select c from GiftCertificate c where c.name = :name", GiftCertificate.class)
                .setParameter("name", name)
                .getResultList().stream().findFirst();
    }
}
