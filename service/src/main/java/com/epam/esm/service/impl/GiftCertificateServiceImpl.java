package com.epam.esm.service.impl;

import com.epam.esm.dao.GiftCertificateDao;
import com.epam.esm.dao.TagDao;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.service.GenericService;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.service.exception.ExceptionErrorCode;
import com.epam.esm.service.exception.PersistentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MultiValueMap;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class GiftCertificateServiceImpl extends GenericService<GiftCertificate> implements GiftCertificateService {

    private final ZoneId zoneId = ZoneId.systemDefault();

    private final GiftCertificateDao giftCertificateDao;
    private final TagDao tagDao;

    @Autowired
    public GiftCertificateServiceImpl(GiftCertificateDao giftCertificateDao, TagDao tagDao) {
        super(giftCertificateDao);
        this.giftCertificateDao = giftCertificateDao;
        this.tagDao = tagDao;
    }

    @Override
    @Transactional
    public GiftCertificate save(GiftCertificate giftCertificate) throws PersistentException {
        Optional<GiftCertificate> existed = giftCertificateDao.findByName(giftCertificate.getName());
        if (existed.isPresent())
            throw new PersistentException(ExceptionErrorCode.DUPLICATED_CERTIFICATE, giftCertificate.getName());
        LocalDateTime localDateTime = LocalDateTime.now(zoneId);
        giftCertificate.setCreateDate(localDateTime);
        giftCertificate.setLastUpdateDate(localDateTime);
        giftCertificate.setTags(updateTagList(giftCertificate.getTags()));
        giftCertificateDao.save(giftCertificate);
        return giftCertificate;
    }

    @Override
    @Transactional
    public GiftCertificate update(GiftCertificate giftCertificate) throws PersistentException {
        GiftCertificate giftCertificateToUpdate = giftCertificateDao
                .findById(giftCertificate.getId())
                .orElseThrow(() -> new PersistentException(ExceptionErrorCode.CERTIFICATE_NOT_FOUND));

        Optional.ofNullable(giftCertificate.getName()).ifPresent(giftCertificateToUpdate::setName);
        Optional.ofNullable(giftCertificate.getDescription()).ifPresent(giftCertificateToUpdate::setDescription);
        Optional.ofNullable(giftCertificate.getPrice()).ifPresent(giftCertificateToUpdate::setPrice);
        Optional.ofNullable(giftCertificate.getDuration()).ifPresent(giftCertificateToUpdate::setDuration);
        if (giftCertificate.getTags() != null) {
            giftCertificateToUpdate.setTags(updateTagList(giftCertificate.getTags()));
        }
        giftCertificate.setLastUpdateDate(LocalDateTime.now(zoneId));
        return giftCertificateDao.update(giftCertificateToUpdate);
    }

    @Override
    public List<GiftCertificate> getAllWithFilter(int page, int size, MultiValueMap<String, String> params) {
        Pageable pageable = PageRequest.of(page - 1, size);
        return giftCertificateDao.findAllWithFilter(pageable, params);
    }

    public List<Tag> updateTagList(List<Tag> list) {
        if (list == null || list.isEmpty())
            return list;
        List<Tag> tagList = new ArrayList<>();
        for (Tag tagFromRequest : list) {
            Optional<Tag> tagFromDb = tagDao.findByName(tagFromRequest.getName());
            if (tagFromDb.isPresent()) {
                tagList.add(tagFromDb.get());
            } else {
                tagList.add(tagFromRequest);
            }
        }
        return tagList;
    }
}
