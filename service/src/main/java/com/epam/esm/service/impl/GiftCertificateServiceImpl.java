package com.epam.esm.service.impl;

import com.epam.esm.dao.GiftCertificateDao;
import com.epam.esm.dao.TagDao;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.service.exception.IncorrectUpdateValueException;
import com.epam.esm.service.exception.PersistentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;

import javax.persistence.PersistenceException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class GiftCertificateServiceImpl implements GiftCertificateService {

    private final ZoneId zoneId = ZoneId.of("UTC");

    private final GiftCertificateDao giftCertificateDao;
    private final TagDao tagDao;

    @Autowired
    public GiftCertificateServiceImpl(GiftCertificateDao giftCertificateDao, TagDao tagDao) {
        this.giftCertificateDao = giftCertificateDao;
        this.tagDao = tagDao;
    }

    @Override
    public List<GiftCertificate> findAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return giftCertificateDao.getAll(pageable);
    }

    @Override
    public GiftCertificate findById(Long id) {
        return giftCertificateDao.getById(id).orElseThrow(PersistenceException::new);
    }

    @Override
    public GiftCertificate save(GiftCertificate giftCertificate) {
        LocalDateTime localDateTime = LocalDateTime.now(zoneId);
        giftCertificate.setCreateDate(localDateTime);
        giftCertificate.setLastUpdateDate(localDateTime);
        giftCertificate.setTags(updateTagList(giftCertificate.getTags()));
        giftCertificateDao.create(giftCertificate);
        return giftCertificate;
    }

    @Override
    public void delete(Long id) throws PersistentException {
        Optional<GiftCertificate> giftCertificateOptional = giftCertificateDao.getById(id);
        if (giftCertificateOptional.isEmpty()) throw new PersistentException();
        giftCertificateDao.delete(id);
    }

    @Override
    public GiftCertificate update(GiftCertificate giftCertificate) throws PersistentException {

        Optional<GiftCertificate> giftCertificateToUpdate = giftCertificateDao.getById(giftCertificate.getId());
        if (giftCertificateToUpdate.isEmpty())
            throw new PersistentException();

        giftCertificate = setUpdatableFields(giftCertificateToUpdate.get(), giftCertificate);
        giftCertificate.setTags(updateTagList(giftCertificate.getTags()));
        return giftCertificateDao.update(giftCertificate);
    }

    @Override
    public List<GiftCertificate> findAllWithFilter(int page, int size, MultiValueMap<String, String> params) {
        Pageable pageable = PageRequest.of(page, size);
        return giftCertificateDao.getAllWithFilter(pageable, params);
    }

    public List<Tag> updateTagList(List<Tag> list) {
        if (list == null || list.isEmpty())
            return list;
        List<Tag> tagList = new ArrayList<>();
        for (Tag tagFromRequest : list) {
            Optional<Tag> tagFromDb = tagDao.getByName(tagFromRequest.getName());
            if (tagFromDb.isPresent()) {
                tagList.add(tagFromDb.get());
            } else {
                tagList.add(tagDao.create(tagFromRequest));
            }
        }
        return tagList;
    }

    public GiftCertificate setUpdatableFields(GiftCertificate updatable, GiftCertificate certificate) {

        if (certificate.getId() != 0) {
            updatable.setId(certificate.getId());
        }

        if (certificate.getName() != null) {
            updatable.setName(certificate.getName());
        }

        if (certificate.getDescription() != null) {
            updatable.setDescription(certificate.getDescription());
        }

        if (certificate.getPrice() != null) {
            updatable.setPrice(certificate.getPrice());
        }

        if (certificate.getDuration() != 0) {
            updatable.setDuration(certificate.getDuration());
        }

        if (certificate.getCreateDate() != null) {
            updatable.setCreateDate(certificate.getCreateDate());
        }

        if (certificate.getTags() != null) {
            updatable.setTags(updateTagList(certificate.getTags()));
        }

        updatable.setLastUpdateDate(LocalDateTime.now(zoneId));

        return updatable;
    }

}
