package com.epam.esm.service.impl;

import com.epam.esm.dao.GiftCertificateDao;
import com.epam.esm.dao.TagDao;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.entity.filter.SearchFilter;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.service.exception.ErrorCodes;
import com.epam.esm.service.exception.PersistenceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class GiftCertificateServiceImpl implements GiftCertificateService {

    private final ZoneId zoneId = ZoneId.of("UTC+2");

    private final TagDao tagDao;
    private final GiftCertificateDao giftCertificateDao;

    @Autowired
    public GiftCertificateServiceImpl(TagDao tagDao, GiftCertificateDao giftCertificateDao) {
        this.tagDao = tagDao;
        this.giftCertificateDao = giftCertificateDao;
    }

    @Override
    public List<GiftCertificate> getAll() {
        List<GiftCertificate> giftCertificates = giftCertificateDao.findAll();
        giftCertificates.forEach(this::getTagsForCertificates);
        return giftCertificates;
    }

    @Override
    public List<GiftCertificate> getAllCertificatesWithFilter(SearchFilter searchFilter) {
        List<GiftCertificate> giftCertificates = giftCertificateDao.findAll(searchFilter);
        giftCertificates.forEach(this::getTagsForCertificates);
        return giftCertificates;
    }

    public void getTagsForCertificates(GiftCertificate giftCertificates) {
        List<Tag> tagsForCertificate = tagDao.findTagsForCertificate(giftCertificates.getId());
        giftCertificates.setTags(tagsForCertificate);
    }

    @Override
    public GiftCertificate getById(long id) {
        Optional<GiftCertificate> giftCertificate = giftCertificateDao.findById(id);
        if (giftCertificate.isEmpty()) {
            throw new PersistenceException(ErrorCodes.CERTIFICATE_NOT_FOUND, id);
        }
        List<Tag> tagsForCertificate = tagDao.findTagsForCertificate(id);
        giftCertificate.get().setTags(tagsForCertificate);
        return giftCertificate.get();
    }

    @Override
    @Transactional
    public GiftCertificate save(GiftCertificate giftCertificate) {
        Optional<GiftCertificate> giftCertificateOptional = giftCertificateDao.findByName(giftCertificate.getName());
        if (giftCertificateOptional.isPresent()) {
            throw new PersistenceException(ErrorCodes.DUPLICATE_CERTIFICATE, giftCertificate.getName());
        }

        LocalDateTime localDateTime = LocalDateTime.now(zoneId);
        giftCertificate.setCreateDate(localDateTime);
        giftCertificate.setLastUpdateDate(localDateTime);

        GiftCertificate savedGiftCertificate = giftCertificateDao.create(giftCertificate);
        long id = savedGiftCertificate.getId();
        if (id == 0){
            throw new PersistenceException(ErrorCodes.SAVE_FAILURE, savedGiftCertificate.getName());
        }

        saveNewTags(savedGiftCertificate);
        assignTagsToCertificate(id, giftCertificate.getTags());
        List<Tag> tagsForCertificate = tagDao.findTagsForCertificate(id);
        savedGiftCertificate.setTags(tagsForCertificate);
        return savedGiftCertificate;
    }

    @Override
    @Transactional
    public void delete(long id) {
        Optional<GiftCertificate> giftCertificate = giftCertificateDao.findById(id);
        if (giftCertificate.isEmpty()) {
           throw new PersistenceException(ErrorCodes.CERTIFICATE_NOT_FOUND, id);
        }
        giftCertificateDao.delete(id);
    }

    @Override
    @Transactional
    public void update(long id, GiftCertificate giftCertificate) {
        var giftCertificateOptional = giftCertificateDao.findById(id);
        if (giftCertificateOptional.isEmpty()) {
            throw new PersistenceException(ErrorCodes.CERTIFICATE_NOT_FOUND, id);
        }
        giftCertificate.setId(id);
        if (giftCertificate.getTags() != null) {
            saveNewTags(giftCertificate);
            tagDao.detachTagsFromCertificate(id);
            assignTagsToCertificate(id, giftCertificate.getTags());
            giftCertificate.setTags(tagDao.findTagsForCertificate(id));
        }
        giftCertificate.setLastUpdateDate(LocalDateTime.now(zoneId));
        giftCertificateDao.update(giftCertificate);
    }

    private void assignTagsToCertificate(long certificateId, List<Tag> tags) {
        if (tags == null || tags.isEmpty()) return;
        List<Tag> tagsToAssign = getIdsForTags(tags);
        for (Tag tag : tagsToAssign) {
            tagDao.assignTagToCertificate(certificateId, tag.getId());
        }
    }

    private List<Tag> getIdsForTags(List<Tag> tags) {
        List<Tag> tagsWithIds = new ArrayList<>(tags.size());
        for (Tag newTag : tags) {
            Optional<Tag> tagWithId = tagDao.findByName(newTag.getName());
            tagWithId.ifPresent(tagsWithIds::add);
        }
        return tagsWithIds;
    }

    private void saveNewTags(GiftCertificate giftCertificate) {
        List<Tag> newTags = giftCertificate.getTags();
        if (newTags == null || newTags.isEmpty())
            return;

        for (Tag newTag : newTags) {
            if (tagDao.findByName(newTag.getName()).isEmpty()) {
                tagDao.create(newTag);
            }
        }
    }
}
