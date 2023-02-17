package com.epam.esm.dao.impl;

import com.epam.esm.config.H2Config;
import com.epam.esm.dao.TagDao;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.entity.filter.SearchFilter;
import com.epam.esm.exception.DaoException;
import com.epam.esm.exception.ErrorCodes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = H2Config.class)
@ActiveProfiles("test")
@Transactional
class GiftCertificateDaoImplTest {

    @Autowired
    GiftCertificateDaoImpl giftCertificateDao;

    @Autowired
    TagDao tagDao;

    GiftCertificate giftCertificate1;
    GiftCertificate giftCertificate2;
    List<GiftCertificate> giftCertificateList;

    @BeforeEach
    void setUp() {
        Tag tag1 = new Tag(1, "tag1");
        Tag tag2 = new Tag(2, "tag2");
        Tag tag3 = new Tag(3, "tag3");
        giftCertificate1 = GiftCertificate.builder()
                .id(1L).name("gift1").description("description")
                .tags(List.of(tag1, tag2, tag3))
                .price(BigDecimal.valueOf(99.99)).duration(30)
                .createDate(LocalDateTime.parse("2023-02-17T10:48:40.303950"))
                .lastUpdateDate(LocalDateTime.parse("2023-02-17T10:48:40.303950"))
                .build();
        giftCertificate2 = GiftCertificate.builder()
                .id(2L).name("gift2").description("description2")
                .tags(List.of(tag2, tag3))
                .price(BigDecimal.valueOf(22.22)).duration(90)
                .createDate(LocalDateTime.parse("2023-02-17T10:48:40.303950"))
                .lastUpdateDate(LocalDateTime.parse("2023-02-17T10:48:40.303950"))
                .build();
        giftCertificateList = List.of(giftCertificate1, giftCertificate2);
    }

    @Test
    void create_ShouldReturnCreatedCertificate() {
        GiftCertificate certificate = new GiftCertificate();
        certificate.setName("name test 11");
        certificate.setDescription("description test 1");
        certificate.setPrice(new BigDecimal(100));
        certificate.setDuration(5);
        certificate.setCreateDate(LocalDateTime.now());
        GiftCertificate createdCertificate = giftCertificateDao.create(certificate);
        assertTrue(createdCertificate.getId() > 0);
    }

    @Test
    void getAll_ShouldReturnAllCertificates_whenSearchFilterPassed() {
        SearchFilter searchFilter = SearchFilter.builder()
                .tagName("tag2")
                .description("description")
                .name("gift")
                .sortByType("ASC")
                .sortBy("NAME")
                .build();
        List<GiftCertificate> giftCertificateListFiltered = giftCertificateDao.getAll(searchFilter);
        giftCertificateListFiltered.forEach(c -> c.setTags(tagDao.getTagsForCertificate(c.getId())));
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
    void getAll_ShouldReturnAllCertificates() {
        List<GiftCertificate> giftCertificates= giftCertificateDao.getAll();
        giftCertificates.forEach(g -> g.setTags(tagDao.getTagsForCertificate(g.getId())));
        assertEquals(giftCertificateList, giftCertificates);
    }

    @Test
    void getById_ShouldReturnCertificate() {
        GiftCertificate giftCertificate = giftCertificateDao.getById(giftCertificate1.getId());
        giftCertificate.setTags(tagDao.getTagsForCertificate(giftCertificate1.getId()));
        assertEquals(giftCertificate1, giftCertificate);
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