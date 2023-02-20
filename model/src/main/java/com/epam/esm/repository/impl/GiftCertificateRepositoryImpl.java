package com.epam.esm.repository.impl;

import com.epam.esm.repository.CustomGiftCertificateRepository;
import com.epam.esm.repository.querybuilder.GiftCertificateQueryBuilder;
import com.epam.esm.entity.GiftCertificate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import org.springframework.util.MultiValueMap;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.util.List;

@Component
public class GiftCertificateRepositoryImpl implements CustomGiftCertificateRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Page<GiftCertificate> findAllWithParameters(MultiValueMap<String, String> params, Pageable pageable) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        GiftCertificateQueryBuilder queryBuilder = new GiftCertificateQueryBuilder(criteriaBuilder);
        CriteriaQuery<GiftCertificate> criteriaQuery = queryBuilder.buildQuery(params);
        TypedQuery<GiftCertificate> query = entityManager.createQuery(criteriaQuery);
        query.setFirstResult(pageable.getPageNumber() * pageable.getPageSize());
        query.setMaxResults(pageable.getPageSize());
        List<GiftCertificate> results = query.getResultList();
        long total = entityManager.createQuery(queryBuilder.countByQuery(params)).getSingleResult();
        return new PageImpl<>(results, pageable, total);
    }
}
