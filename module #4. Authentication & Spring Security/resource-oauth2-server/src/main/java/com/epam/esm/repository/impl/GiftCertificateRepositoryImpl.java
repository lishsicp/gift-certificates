package com.epam.esm.repository.impl;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.repository.CustomGiftCertificateRepository;
import com.epam.esm.repository.querybuilder.GiftCertificateQueryBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.MultiValueMap;

import java.util.List;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaQuery;

@Repository
public class GiftCertificateRepositoryImpl implements CustomGiftCertificateRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Page<GiftCertificate> findAllWithParameters(MultiValueMap<String, String> params, Pageable pageable) {
        var criteriaBuilder = entityManager.getCriteriaBuilder();
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
