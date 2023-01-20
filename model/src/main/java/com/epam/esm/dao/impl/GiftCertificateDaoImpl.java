package com.epam.esm.dao.impl;

import com.epam.esm.dao.GenericDao;
import com.epam.esm.dao.GiftCertificateDao;
import com.epam.esm.entity.GiftCertificate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

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
}
