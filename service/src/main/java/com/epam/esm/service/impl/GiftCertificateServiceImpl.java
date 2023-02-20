package com.epam.esm.service.impl;

import com.epam.esm.repository.GiftCertificateRepository;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.service.exception.ExceptionErrorCode;
import com.epam.esm.service.exception.PersistentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class GiftCertificateServiceImpl implements GiftCertificateService {

    private final ZoneId zoneId = ZoneId.systemDefault();

    private final GiftCertificateRepository giftCertificateDao;
    private final TagRepository tagDao;

    @Autowired
    public GiftCertificateServiceImpl(GiftCertificateRepository giftCertificateDao, TagRepository tagDao) {
        this.giftCertificateDao = giftCertificateDao;
        this.tagDao = tagDao;
    }

    @Override
    public Page<GiftCertificate> getAll(int page, int size) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Page<GiftCertificate> getAllWithFilter(int page, int size, MultiValueMap<String, String> params) {
        Pageable pageable = PageRequest.of(page - 1, size);
        return giftCertificateDao.findAllWithParameters(params, pageable);
    }

    @Override
    public GiftCertificate getById(Long id) throws PersistentException {
        return giftCertificateDao.findById(id).orElseThrow(() -> new PersistentException(ExceptionErrorCode.RESOURCE_NOT_FOUND, id));
    }

    @Override
    public GiftCertificate save(GiftCertificate giftCertificate) throws PersistentException {
        Optional<GiftCertificate> existed = giftCertificateDao.findGiftCertificateByName(giftCertificate.getName());
        if (existed.isPresent())
            throw new PersistentException(ExceptionErrorCode.DUPLICATED_CERTIFICATE, giftCertificate.getName());
        LocalDateTime localDateTime = LocalDateTime.now(zoneId);
        giftCertificate.setCreateDate(localDateTime);
        giftCertificate.setLastUpdateDate(localDateTime);
        giftCertificate.setTags(updateTagList(giftCertificate.getTags()));
        return giftCertificateDao.save(giftCertificate);
    }

    @Override
    public void delete(Long id) throws PersistentException {
        Optional<GiftCertificate> giftCertificateOptional = giftCertificateDao.findById(id);
        if (giftCertificateOptional.isEmpty())
            throw new PersistentException(ExceptionErrorCode.RESOURCE_NOT_FOUND, id);
        giftCertificateDao.delete(giftCertificateOptional.get());
    }

    @Override
    public GiftCertificate update(Long id, GiftCertificate giftCertificate) throws PersistentException {
        GiftCertificate giftCertificateToUpdate = giftCertificateDao
                .findById(id)
                .orElseThrow(() -> new PersistentException(ExceptionErrorCode.CERTIFICATE_NOT_FOUND));
        Optional.ofNullable(giftCertificate.getName()).ifPresent(giftCertificateToUpdate::setName);
        Optional.ofNullable(giftCertificate.getDescription()).ifPresent(giftCertificateToUpdate::setDescription);
        Optional.ofNullable(giftCertificate.getPrice()).ifPresent(giftCertificateToUpdate::setPrice);
        Optional.ofNullable(giftCertificate.getDuration()).ifPresent(giftCertificateToUpdate::setDuration);
        if (giftCertificate.getTags() != null) {
            giftCertificateToUpdate.setTags(updateTagList(giftCertificate.getTags()));
        }
        giftCertificateToUpdate.setLastUpdateDate(LocalDateTime.now(zoneId));
        return giftCertificateDao.save(giftCertificateToUpdate);
    }

    public List<Tag> updateTagList(List<Tag> list) {
        if (list == null || list.isEmpty())
            return list;
        List<Tag> tagList = new ArrayList<>();
        for (Tag tagFromRequest : list) {
            Optional<Tag> tagFromDb = tagDao.findTagByName(tagFromRequest.getName());
            if (tagFromDb.isPresent()) {
                tagList.add(tagFromDb.get());
            } else {
                tagList.add(tagFromRequest);
            }
        }
        return tagList;
    }
}
