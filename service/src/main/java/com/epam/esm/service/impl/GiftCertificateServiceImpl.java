package com.epam.esm.service.impl;

import com.epam.esm.dao.GiftCertificateDao;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.service.exception.IncorrectUpdateValueException;
import com.epam.esm.service.validator.GiftCertificateUpdateValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Service
public class GiftCertificateServiceImpl implements GiftCertificateService {

    private final ZoneId zoneId = ZoneId.of("UTC");

    private final GiftCertificateDao giftCertificateDao;

    @Autowired
    public GiftCertificateServiceImpl(GiftCertificateDao giftCertificateDao) {
        this.giftCertificateDao = giftCertificateDao;
    }

    @Override
    public List<GiftCertificate> findAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return giftCertificateDao.getAll(pageable);
    }

    @Override
    public GiftCertificate findById(Long id) {
        return giftCertificateDao.getById(id);
    }

    @Override
    public GiftCertificate save(GiftCertificate giftCertificate) {
        LocalDateTime localDateTime = LocalDateTime.now(zoneId);
        giftCertificate.setCreateDate(localDateTime);
        giftCertificate.setLastUpdateDate(localDateTime);
        giftCertificateDao.create(giftCertificate);
        return giftCertificate;
    }

    @Override
    public void delete(Long id) {
        giftCertificateDao.delete(giftCertificateDao.getById(id));
    }

    @Override
    public GiftCertificate update(GiftCertificate giftCertificate) throws IncorrectUpdateValueException {
        giftCertificate.setLastUpdateDate(LocalDateTime.now(zoneId));
        return giftCertificateDao.update(giftCertificate);
    }
}
