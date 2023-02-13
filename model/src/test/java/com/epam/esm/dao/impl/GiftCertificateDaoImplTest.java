package com.epam.esm.dao.impl;

import com.epam.esm.config.H2Config;
import com.epam.esm.dao.TagDao;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.filter.SearchFilter;
import com.epam.esm.exception.DaoException;
import com.epam.esm.exception.ErrorCodes;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = H2Config.class)
@ActiveProfiles("test")
class GiftCertificateDaoImplTest {

    @Autowired
    GiftCertificateDaoImpl giftCertificateDao;

    @Autowired
    TagDao tagDao;

    @Test
    void create_ShouldReturnCreatedCertificate() {
        GiftCertificate certificate = new GiftCertificate();
        certificate.setName("name test 11");
        certificate.setDescription("description test 1");
        certificate.setPrice(new BigDecimal(100));
        certificate.setDuration(5);
        certificate.setCreateDate(LocalDateTime.now());
        giftCertificateDao.create(certificate);
        assertTrue(certificate.getId() > 0);
    }

    @Test
    void getAll_ShouldReturnAllCertificates_whenSearchFilterPassed() {
        SearchFilter searchFilter = SearchFilter.builder()
                .tagName("tag2")
                .description("description")
                .name("gift")
                .sortByType("DESC")
                .sortBy("NAME")
                .build();
        List<GiftCertificate> giftCertificateList = giftCertificateDao.getAll(searchFilter);
        giftCertificateList.forEach(c -> c.setTags(tagDao.getTagsForCertificate(c.getId())));
        long actualAmount = giftCertificateList.size();
        long checkedAmount = giftCertificateList.stream()
                .filter(c -> c.getName().contains("gift"))
                .filter(c -> c.getDescription().contains("description"))
                .filter(c -> c.getTags().stream().anyMatch(tag -> tag.getName().contains("tag2")))
                .count();
        assertEquals(checkedAmount, actualAmount);
    }

    @Test
    void getAll_ShouldReturnNotEmptyList() {
        List<GiftCertificate> giftCertificateList = giftCertificateDao.getAll();
        assertTrue(giftCertificateList.size() > 0);
    }

    @Test
    void getById_ShouldReturnCertificate() {
        GiftCertificate giftCertificate = giftCertificateDao.getById(1L);
        assertEquals(1L, giftCertificate.getId());
    }

    @Test
    void update_ShouldUpdateOnlyCertificatesName() {
        GiftCertificate initialCertificate = new GiftCertificate();
        initialCertificate.setName("updatesOnlyCertificatesName");
        initialCertificate.setDescription("initialDescription");
        initialCertificate.setPrice(BigDecimal.ONE);
        initialCertificate.setDuration(2);
        initialCertificate.setCreateDate(LocalDateTime.now());
        long id = giftCertificateDao.create(initialCertificate).getId();

        GiftCertificate update = new GiftCertificate();
        update.setId(id);
        update.setName("updatedName");
        update.setLastUpdateDate(initialCertificate.getCreateDate());
        giftCertificateDao.update(update);

        initialCertificate = giftCertificateDao.getById(id);
        giftCertificateDao.remove(id);
        assertEquals(update.getName(), initialCertificate.getName());
    }

    @Test
    void update_ShouldUpdateAllFields() {
        GiftCertificate initialCertificate = new GiftCertificate();
        initialCertificate.setName("UpdatesAllFields");
        initialCertificate.setDescription("initialDescription");
        initialCertificate.setPrice(BigDecimal.ONE);
        initialCertificate.setDuration(2);
        initialCertificate.setCreateDate(LocalDateTime.now());
        long id = giftCertificateDao.create(initialCertificate).getId();

        GiftCertificate update = new GiftCertificate();
        update.setId(id);
        update.setDescription("updatedDescription");
        update.setPrice(BigDecimal.TEN);
        update.setDuration(3);
        LocalDateTime updatedLastCreatedDate = LocalDateTime.now();
        update.setLastUpdateDate(updatedLastCreatedDate);
        giftCertificateDao.update(update);

        GiftCertificate updatedCertificate = giftCertificateDao.getById(id);
        assertNotSame(update.getName(), updatedCertificate.getName());
        assertEquals(update.getDescription(), updatedCertificate.getDescription());
        assertEquals(update.getDuration(), updatedCertificate.getDuration());
        assertEquals(0, updatedCertificate.getPrice().compareTo(update.getPrice()));
        assertEquals(update.getLastUpdateDate().truncatedTo(ChronoUnit.MILLIS), updatedCertificate.getLastUpdateDate().truncatedTo(ChronoUnit.MILLIS));
    }

    @Test
    void remove_shouldThrowException_whenCertificateHasBeenRemoved() {
        GiftCertificate initialCertificate = new GiftCertificate();
        initialCertificate.setName("testDelete");
        initialCertificate.setDescription("testDelete");
        initialCertificate.setPrice(BigDecimal.ONE);
        initialCertificate.setDuration(2);
        initialCertificate.setCreateDate(LocalDateTime.now());
        long id = giftCertificateDao.create(initialCertificate).getId();

        giftCertificateDao.remove(id);

        Executable getByIdExec = () -> giftCertificateDao.getById(id);
        DaoException ex = assertThrows(DaoException.class, getByIdExec);
        assertThrows(DaoException.class,() -> giftCertificateDao.getById(id));
        assertEquals(ErrorCodes.CERTIFICATE_NOT_FOUND, ex.getErrorCode());
    }
}