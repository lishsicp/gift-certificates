package com.epam.esm.dao.impl;

import com.epam.esm.config.TestDaoConfig;
import com.epam.esm.dao.GenericDao;
import com.epam.esm.dao.GiftCertificateDao;
import com.epam.esm.dao.TagDao;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.ConfigDataApplicationContextInitializer;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import javax.transaction.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = TestDaoConfig.class)
@ContextConfiguration(initializers = ConfigDataApplicationContextInitializer.class)
@ActiveProfiles("test")
@Transactional
class GiftCertificateDaoImplTest {

    private static final GiftCertificate CERTIFICATE_1 = GiftCertificate.builder()
            .id(1L).name("GiftCertificate1").description("Description")
            .price(new BigDecimal("500.00")).duration(60L)
            .createDate(LocalDateTime.parse("2022-12-15T11:43:33"))
            .lastUpdateDate((LocalDateTime.parse("2022-12-15T11:43:33")))
            .tags(Collections.singletonList(Tag.builder().id(2L).name("tag2").build()))
            .build();

    private static final GiftCertificate CERTIFICATE_2 = GiftCertificate.builder()
            .id(2L).name("GiftCertificate2").description("Description")
            .price(new BigDecimal("200.00")).duration(1L)
            .createDate(LocalDateTime.parse("2023-01-25T13:56:30"))
            .lastUpdateDate((LocalDateTime.parse("2023-01-25T13:56:30")))
            .tags(Arrays.asList(Tag.builder().id(2L).name("tag2").build(), Tag.builder().id(4L).name("tag4").build()))
            .build();

    private static final GiftCertificate CERTIFICATE_3 = GiftCertificate.builder()
            .id(3L).name("GiftCertificate3").description("Description")
            .price(new BigDecimal("300.00")).duration(90L)
            .createDate(LocalDateTime.parse("2023-01-30T09:08:56"))
            .lastUpdateDate((LocalDateTime.parse("2023-01-30T09:08:56")))
            .tags(Arrays.asList(Tag.builder().id(2L).name("tag2").build(), Tag.builder().id(4L).name("tag4").build()))
            .build();

    private static final Pageable PAGE_REQUEST = PageRequest.of(0, 5);

    private static final long NON_EXISTENT_CERT_ID = 999L;

    private static final String FILTER_NAME_KEY = "name";
    private static final String FILTER_NAME_VALUE = "Gift";
    private static final String FILTER_DESCRIPTION_KEY = "description";
    private static final String FILTER_DESCRIPTION_VALUE = "Desc";
    private static final String FILTER_DATE_SORT_KEY = "date_sort";
    private static final String FILTER_NAME_SORT_KEY = "name_sort";
    private static final String FILTER_DATE_SORT_VALUE = "asc";
    private static final String FILTER_NAME_SORT_VALUE = "desc";
    private static final String FILTER_TAGS_KEY = "tags";
    private static final String FILTER_TAGS_VALUE_1 = "tag2";
    private static final String FILTER_TAGS_VALUE_2 = "tag4";
    private static final String WRONG_FILTER_KEY = "Wrong key";
    private static final String WRONG_FILTER_VALUE = "Wrong value";


    @Autowired
    GiftCertificateDao giftCertificateDao;


    @Test
    void testFindAllShouldReturnAll() {
        List<GiftCertificate> giftCertificates = giftCertificateDao.findAll(PAGE_REQUEST);
        List<GiftCertificate> expected = Arrays.asList(CERTIFICATE_1,CERTIFICATE_2,CERTIFICATE_3);
        assertIterableEquals(expected, giftCertificates);
    }

    @Test
    void testFindByIdAShouldReturnOne() {
        Optional<GiftCertificate> giftCertificateOptional = giftCertificateDao.findById(CERTIFICATE_1.getId());
        assertTrue(giftCertificateOptional.isPresent());
        assertEquals(CERTIFICATE_1, giftCertificateOptional.get());
    }

    @Test
    void testFindByIdAShouldBeEmpty() {
        Optional<GiftCertificate> giftCertificateOptional = giftCertificateDao.findById(NON_EXISTENT_CERT_ID);
        assertTrue(giftCertificateOptional.isEmpty());
    }

    @Test
    void testDeleteShouldBeEmpty() {
        GiftCertificate testCert = GiftCertificate.builder()
                .name("TestDelete").description("TestDelete")
                .price(new BigDecimal("200.00")).duration(1L)
                .createDate(LocalDateTime.parse("2023-01-25T13:56:30"))
                .lastUpdateDate((LocalDateTime.parse("2023-01-25T13:56:30")))
                .tags(Arrays.asList(Tag.builder().name("testTag1").build(), Tag.builder().name("testTag2").build()))
                .build();
        GiftCertificate testCertWithId = giftCertificateDao.save(testCert);
        giftCertificateDao.delete(testCertWithId.getId());
        assertTrue(giftCertificateDao.findById(testCertWithId.getId()).isEmpty());
    }

    @Test
    void testDeleteShouldThrowException() {
        assertThrows(Exception.class, ()-> giftCertificateDao.delete(NON_EXISTENT_CERT_ID));
    }

    @Test
    void testSaveShouldSaveAndGenerateId() {
        GiftCertificate testCert = GiftCertificate.builder()
                .name("TestSave").description("TestSave")
                .price(new BigDecimal("200.00")).duration(1L)
                .createDate(LocalDateTime.parse("2023-01-25T13:56:30"))
                .lastUpdateDate((LocalDateTime.parse("2023-01-25T13:56:30")))
                .tags(Arrays.asList(Tag.builder().name("testTag1").build(), Tag.builder().name("testTag2").build()))
                .build();
        testCert = giftCertificateDao.save(testCert);
        assertNotNull(testCert.getOperation());
        assertNotNull(testCert.getTimestamp());
        assertTrue(testCert.getId() > 0);
        assertTrue(testCert.getTags().size() > 0);
    }

    @Test
    void testUpdateShouldUpdateName() {
        Optional<GiftCertificate> certificateToUpdate = giftCertificateDao.findById(1L);
        String oldName;
        String updatedName = "updatedName";
        if (certificateToUpdate.isPresent()) {
            oldName = certificateToUpdate.get().getName();
            certificateToUpdate.get().setName(updatedName);
            certificateToUpdate.get().setLastUpdateDate(LocalDateTime.now());
            GiftCertificate updated = giftCertificateDao.update(certificateToUpdate.get());
            assertNotSame(oldName, updated.getName());
            assertTrue(giftCertificateDao.findByName(updatedName).isPresent());
        }
    }

    @Test
    void testFindAllWithValidFilterShouldReturnTwo() {
        MultiValueMap<String, String> filterParams = new LinkedMultiValueMap<>();
        filterParams.add(FILTER_NAME_KEY, FILTER_NAME_VALUE);
        filterParams.add(FILTER_DESCRIPTION_KEY, FILTER_DESCRIPTION_VALUE);
        filterParams.add(FILTER_TAGS_KEY, FILTER_TAGS_VALUE_1);
        filterParams.add(FILTER_TAGS_KEY, FILTER_TAGS_VALUE_2);
        filterParams.add(FILTER_DATE_SORT_KEY, FILTER_DATE_SORT_VALUE);
        filterParams.add(FILTER_NAME_SORT_KEY, FILTER_NAME_SORT_VALUE);

        List<GiftCertificate> expected = Arrays.asList(CERTIFICATE_2, CERTIFICATE_3);
        List<GiftCertificate> actual = giftCertificateDao.findAllWithFilter(PAGE_REQUEST, filterParams);

        assertEquals(expected, actual);
    }

    @Test
    void testFindAllWithInvalidFilterShouldReturnAll() {
        MultiValueMap<String, String> filterParams = new LinkedMultiValueMap<>();
        filterParams.add(WRONG_FILTER_KEY, WRONG_FILTER_VALUE);

        List<GiftCertificate> expected = Arrays.asList(CERTIFICATE_1, CERTIFICATE_2, CERTIFICATE_3);
        List<GiftCertificate> actual = giftCertificateDao.findAllWithFilter(PAGE_REQUEST, filterParams);

        assertEquals(expected, actual);
    }

    @Test
    void testFindByNameShouldBePresent() {
        assertTrue(giftCertificateDao.findByName(CERTIFICATE_1.getName()).isPresent());
    }

    @Test
    void testFindByNameShouldBeEmpty() {
        assertTrue(giftCertificateDao.findByName(CERTIFICATE_1.getName() + "POSTFIX").isEmpty());
    }
}