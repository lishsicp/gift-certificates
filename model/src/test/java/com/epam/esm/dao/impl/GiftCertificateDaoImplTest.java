package com.epam.esm.dao.impl;

import com.epam.esm.config.H2Config;
import com.epam.esm.dao.GiftCertificateDao;
import com.epam.esm.dao.TagDao;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.filter.SearchFilter;
import com.epam.esm.util.ModelFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = H2Config.class)
@ActiveProfiles("test")
@Transactional
class GiftCertificateDaoImplTest {

    @Autowired
    private GiftCertificateDao giftCertificateDao;

    @Autowired
    private TagDao tagDao;

    @Test
    void create_shouldReturnCreatedCertificate() {
        GiftCertificate certificate = ModelFactory.createGiftCertificate();
        certificate.setName("anyCertificateName");
        GiftCertificate createdCertificate = giftCertificateDao.create(certificate);
        assertTrue(createdCertificate.getId() > 0);
    }

    @Test
    void findAll_shouldReturnAllCertificates_whenSearchFilterPassed() {
        var giftCertificateList = List.of(
                ModelFactory.createGiftCertificate(1, 1, 2, 3),
                ModelFactory.createGiftCertificate(2, 2, 3)
        );

        SearchFilter searchFilter = SearchFilter.builder()
                .tagName("tag2")
                .description("description")
                .name("gift")
                .sortByType("ASC")
                .sortBy("NAME")
                .build();
        List<GiftCertificate> giftCertificateListFiltered = giftCertificateDao.findAll(searchFilter);
        giftCertificateListFiltered.forEach(c -> c.setTags(tagDao.findTagsForCertificate(c.getId())));
        long actualAmount = giftCertificateList.size();
        long checkedAmount = giftCertificateListFiltered.stream()
                .filter(c -> c.getName().contains("gift"))
                .filter(c -> c.getDescription().contains("description"))
                .filter(c -> c.getTags().stream().anyMatch(tag -> tag.getName().contains("tag2")))
                .count();
        assertEquals(checkedAmount, actualAmount);
        assertEquals(giftCertificateList, giftCertificateListFiltered);
    }

    @Test
    void findAll_shouldReturnAllCertificates() {
        var giftCertificateList = List.of(
                ModelFactory.createGiftCertificate(1, 1, 2, 3),
                ModelFactory.createGiftCertificate(2, 2, 3)
        );
        List<GiftCertificate> giftCertificates= giftCertificateDao.findAll();
        giftCertificates.forEach(g -> g.setTags(tagDao.findTagsForCertificate(g.getId())));
        assertEquals(giftCertificateList, giftCertificates);
    }

    @Test
    void findById_shouldReturnCertificate() {
        long id = 1;
        var expected = Optional.of(ModelFactory.createGiftCertificate(id, 1, 2, 3));

        Optional<GiftCertificate> actual = giftCertificateDao.findById(id);

        assertTrue(actual.isPresent());

        actual.get().setTags(tagDao.findTagsForCertificate(id));

        assertEquals(expected, actual);
    }

    @Test
    void findByName_shouldReturnCertificate() {
        long id = 1;

        var expected = Optional.of(ModelFactory.createGiftCertificate(id));
        Optional<GiftCertificate> actual = giftCertificateDao.findByName(expected.get().getName());

        assertTrue(actual.isPresent());
        assertEquals(expected, actual);
    }

    @Test
    void update_shouldUpdateOnlyCertificatesName() {
        long id = 1;
        GiftCertificate update = ModelFactory.createGiftCertificate(id);
        update.setName("updatedName");
        giftCertificateDao.update(update);

        var giftCertificate = giftCertificateDao.findById(id);
        assertTrue(giftCertificate.isPresent());
        assertEquals(update.getName(), giftCertificate.get().getName());
    }

    @Test
    void update_shouldUpdateAllFields() {
        long id = 1;
        GiftCertificate initialCertificate = ModelFactory.createGiftCertificate(id);
        GiftCertificate update = ModelFactory.createGiftCertificate(id);
        update.setName("updatedName");
        update.setDescription("updatedDescription");
        update.setPrice(BigDecimal.TEN);
        update.setDuration(3);
        giftCertificateDao.update(update);

        Optional<GiftCertificate> updatedCertificate = giftCertificateDao.findById(id);

        assertTrue(updatedCertificate.isPresent());
        assertNotSame(update.getName(), updatedCertificate.get().getName());
        assertEquals(update.getDescription(), updatedCertificate.get().getDescription());
        assertEquals(update.getDuration(), updatedCertificate.get().getDuration());
        assertEquals(0, initialCertificate.getPrice().compareTo(updatedCertificate.get().getPrice()));
    }

    @Test
    void delete_shouldDeleteCertificate() {
        GiftCertificate initialCertificate = ModelFactory.createGiftCertificate();
        long id = giftCertificateDao.create(initialCertificate).getId();
        giftCertificateDao.delete(id);
        assertTrue(giftCertificateDao.findById(id).isEmpty());
    }
}