package com.epam.esm.service.impl;

import com.epam.esm.dao.GiftCertificateDao;
import com.epam.esm.dao.TagDao;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.entity.filter.SearchFilter;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.service.exception.IncorrectUpdateValueException;
import com.epam.esm.service.validator.GiftCertificateUpdateValidator;
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

    private final ZoneId zoneId = ZoneId.of("UTC");

    private final TagDao tagDao;
    private final GiftCertificateDao giftCertificateDao;

    @Autowired
    public GiftCertificateServiceImpl(TagDao tagDao, GiftCertificateDao giftCertificateDao) {
        this.tagDao = tagDao;
        this.giftCertificateDao = giftCertificateDao;
    }

    @Override
    public List<GiftCertificate> findAll() {
        List<GiftCertificate> giftCertificates = giftCertificateDao.getAll();
        giftCertificates.forEach(this::getTagsForCertificates);
        return giftCertificates;
    }

    @Override
    public List<GiftCertificate> findAllCertificatesWithFilter(SearchFilter searchFilter) {
        List<GiftCertificate> giftCertificates = giftCertificateDao.getAll(searchFilter);
        giftCertificates.forEach(this::getTagsForCertificates);
        return giftCertificates;
    }

    public void getTagsForCertificates(GiftCertificate giftCertificates) {
        List<Tag> tagsForCertificate = tagDao.getTagsForCertificate(giftCertificates.getId());
        giftCertificates.setTags(tagsForCertificate);
    }

    @Override
    public GiftCertificate findById(Long id) {
        GiftCertificate giftCertificate = giftCertificateDao.getById(id);
        List<Tag> tagsForCertificate = tagDao.getTagsForCertificate(id);
        giftCertificate.setTags(tagsForCertificate);
        return giftCertificate;
    }

    @Override
    @Transactional
    public GiftCertificate save(GiftCertificate giftCertificate) {
        LocalDateTime localDateTime = LocalDateTime.now(zoneId);
        giftCertificate.setCreateDate(localDateTime);
        giftCertificate.setLastUpdateDate(localDateTime);

        GiftCertificate savedGiftCertificate = giftCertificateDao.create(giftCertificate);
        long id = savedGiftCertificate.getId();

        saveNewTags(savedGiftCertificate);
        assignTagsToCertificate(id, giftCertificate.getTags());
        List<Tag> tagsForCertificate = tagDao.getTagsForCertificate(id);
        savedGiftCertificate.setTags(tagsForCertificate);
        return savedGiftCertificate;
    }

    @Override
    @Transactional
    public void delete(Long id) {
        giftCertificateDao.remove(id);
    }

    @Override
    @Transactional
    public void update(GiftCertificate giftCertificate) throws IncorrectUpdateValueException {
        GiftCertificateUpdateValidator.validate(giftCertificate);
        long certificateId = giftCertificate.getId();
        if (giftCertificate.getTags() != null) {
            saveNewTags(giftCertificate);
            tagDao.detachTagsFromCertificate(certificateId);
            assignTagsToCertificate(certificateId, giftCertificate.getTags());
            giftCertificate.setTags(tagDao.getTagsForCertificate(certificateId));
        }
        giftCertificate.setLastUpdateDate(LocalDateTime.now(zoneId));
        giftCertificateDao.update(giftCertificate);
    }

    private void assignTagsToCertificate(Long certificateId, List<Tag> tags) {
        if (tags == null || tags.isEmpty()) return;
        List<Tag> tagsToAssign = getIdsForTags(tags);
        for (Tag tag : tagsToAssign) {
            tagDao.assignTagToCertificate(certificateId, tag.getId());
        }
    }

    private List<Tag> getIdsForTags(List<Tag> tags) {
        List<Tag> tagsWithIds = new ArrayList<>(tags.size());
        for (Tag newTag : tags) {
            Optional<Tag> tagWithId = tagDao.getByName(newTag.getName());
            tagWithId.ifPresent(tagsWithIds::add);
        }
        return tagsWithIds;
    }

    private void saveNewTags(GiftCertificate giftCertificate) {
        List<Tag> newTags = giftCertificate.getTags();
        if (newTags == null || newTags.isEmpty())
            return;

        List<Tag> allTags = tagDao.getAll();

        for (Tag newTag : newTags) {
            boolean isExist = false;
            for (Tag tag : allTags) {
                if (newTag.getName().equalsIgnoreCase(tag.getName())) {
                    isExist = true;
                    break;
                }
            }
            if (!isExist) {
                tagDao.create(newTag);
            }
        }
    }
}
