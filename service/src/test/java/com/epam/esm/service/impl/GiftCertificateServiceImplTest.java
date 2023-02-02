package com.epam.esm.service.impl;

import com.epam.esm.dao.GiftCertificateDao;
import com.epam.esm.dao.TagDao;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.service.exception.PersistentException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.util.MultiValueMap;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class GiftCertificateServiceImplTest {

    @Mock
    private GiftCertificateDao giftCertificateDao;
    @Mock
    private TagDao tagDao;

    @InjectMocks
    private GiftCertificateServiceImpl service;

    private GiftCertificate GIFT_CERTIFICATE_1;
    private GiftCertificate GIFT_CERTIFICATE_2;

    private List<Tag> TAG_LIST;

    @BeforeEach
    void setUp() {
        TAG_LIST = Arrays.asList(
                Tag.builder().id(1L).name("tag1").build(),
                Tag.builder().id(2L).name("tag2").build(),
                Tag.builder().id(3L).name("tag3").build()
        );

        GIFT_CERTIFICATE_1 = GiftCertificate.builder()
                .id(1L).name("GiftCertificate1").description("Description")
                .price(new BigDecimal("500.00")).duration(60L)
                .createDate(LocalDateTime.parse("2022-12-15T11:43:33"))
                .lastUpdateDate((LocalDateTime.parse("2022-12-15T11:43:33")))
                .tags(TAG_LIST)
                .build();
        GIFT_CERTIFICATE_2 = GiftCertificate.builder()
                .id(2L).name("GiftCertificate2").description("Description")
                .price(new BigDecimal("200.00")).duration(1L)
                .createDate(LocalDateTime.parse("2023-01-25T13:56:30"))
                .lastUpdateDate((LocalDateTime.parse("2023-01-25T13:56:30")))
                .tags(TAG_LIST)
                .build();
    }

    @Test
    void testFindAll_ShouldReturnTwoGiftCertificates() {
        List<GiftCertificate> expected = Arrays.asList(GIFT_CERTIFICATE_1, GIFT_CERTIFICATE_2);
        when(giftCertificateDao.findAll(any())).thenReturn(expected);
        List<GiftCertificate> actual = service.getAll(1, 5);
        assertEquals(expected, actual);
    }

    @Test
    void testFindById_ShouldReturnGiftCertificate() {
        when(giftCertificateDao.findById(any())).thenReturn(Optional.ofNullable(GIFT_CERTIFICATE_1));
        GiftCertificate certificateById = service.getById(GIFT_CERTIFICATE_1.getId());
        assertEquals(GIFT_CERTIFICATE_1, certificateById);
    }

    @Test
    void testFindById_ShouldThrowException() {
        Long certId = GIFT_CERTIFICATE_1.getId();
        assertThrows(PersistentException.class, () -> service.getById(certId));
    }

    @Nested
    class WhenDeleting {

        @Mock
        private GiftCertificate giftCertificate;

        @Test
        void testDelete_ShouldInvokeGiftCertificateDaoDelete() {
            when(giftCertificateDao.findById(anyLong())).thenReturn(Optional.of(giftCertificate));
            doNothing().when(giftCertificateDao).delete(any());
            service.delete(1L);
            verify(giftCertificateDao, times(1)).delete(any());
        }

        @Test
        void testDelete_ShouldThrowException() {
            assertThrows(PersistentException.class, () -> service.delete(1L));
        }
    }

    @Nested
    class WhenSaving {

        @Mock
        private GiftCertificate GIFT_CERTIFICATE_TO_SAVE;

        @BeforeEach
        void setup() {
            GIFT_CERTIFICATE_TO_SAVE = GiftCertificate.builder()
                    .name("GiftCertificate1").description("Description")
                    .price(new BigDecimal("500.00")).duration(60L)
                    .tags(Arrays.asList(Tag.builder().name("tag1").build(), Tag.builder().name("tag2").build(), Tag.builder().name("tag3").build()))
                    .build();
        }

        @Test
        void testSave_ShouldSave() {
            when(giftCertificateDao.findByName(anyString())).thenReturn(Optional.empty());
            when(giftCertificateDao.save(any())).thenReturn(GIFT_CERTIFICATE_1);
            GiftCertificate actual = service.save(GIFT_CERTIFICATE_TO_SAVE);
            assertEquals(GIFT_CERTIFICATE_1, actual);
        }

        @Test
        void testSave_ShouldThrowException() {
            when(giftCertificateDao.findByName(anyString())).thenReturn(Optional.of(GIFT_CERTIFICATE_2));
            assertThrows(PersistentException.class, () -> service.save(GIFT_CERTIFICATE_TO_SAVE));
        }
    }

    @Nested
    class WhenUpdating {

        private GiftCertificate BEFORE_UPDATE;

        private GiftCertificate UPDATABLE_DATA;

        private GiftCertificate AFTER_UPDATE;

        @BeforeEach
        void setup() {
            BEFORE_UPDATE = GiftCertificate.builder()
                    .id(2L).name("GiftCertificate2").description("Description")
                    .price(new BigDecimal("200.00")).duration(1L)
                    .createDate(LocalDateTime.parse("2023-01-25T13:56:30"))
                    .lastUpdateDate((LocalDateTime.parse("2023-01-25T13:56:30")))
                    .tags(TAG_LIST)
                    .build();
            UPDATABLE_DATA = GiftCertificate.builder()
                    .id(2L).name("updateName")
                    .tags(Collections.singletonList(Tag.builder().id(1L).name("tag1").build()))
                    .build();
            AFTER_UPDATE = GiftCertificate.builder()
                    .id(2L).name("updateName").description("Description")
                    .price(new BigDecimal("200.00")).duration(1L)
                    .createDate(LocalDateTime.parse("2023-01-25T13:56:30"))
                    .lastUpdateDate((LocalDateTime.parse("2023-01-25T13:56:30")))
                    .tags(Collections.singletonList(Tag.builder().id(1L).name("tag1").build()))
                    .build();
        }

        @Test
        void testUpdate() {
            when(giftCertificateDao.findById(any())).thenReturn(Optional.of(BEFORE_UPDATE));
            when(giftCertificateDao.update(any())).thenReturn(AFTER_UPDATE);
            GiftCertificate updatedCertificate = service.update(BEFORE_UPDATE.getId(), UPDATABLE_DATA);
            assertEquals(AFTER_UPDATE, updatedCertificate);
            assertNotSame(BEFORE_UPDATE, updatedCertificate);
        }

        @Test
        void updateNotExistedEntity_thenThrowEx() {
            Long certId = GIFT_CERTIFICATE_1.getId();
            assertThrows(PersistentException.class, () -> service.update(certId, GIFT_CERTIFICATE_1));
        }
    }

    @Nested
    class WhenGettingAllWithFilter {

        @Mock
        private MultiValueMap<String, String> params;

        @Test
        void testFindAllWithFilter_ShouldReturnTwoGiftCertificates() {
            List<GiftCertificate> expected = Arrays.asList(GIFT_CERTIFICATE_1, GIFT_CERTIFICATE_2);
            int PAGE = 37;
            int SIZE = 56;
            when(giftCertificateDao.findAllWithFilter(PageRequest.of(PAGE - 1, SIZE), params)).thenReturn(expected);
            List<GiftCertificate> actual = service.getAllWithFilter(PAGE, SIZE, params);
            assertEquals(expected, actual);
        }
    }

    @Nested
    class WhenUpdatingTagList {

        private Tag TAG_TO_UPDATE_1;
        private Tag TAG_TO_UPDATE_2;
        private Tag TAG_TO_UPDATE_NEW;
        private List<Tag> LIST;

        @BeforeEach
        void setup() {
            TAG_TO_UPDATE_1 = Tag.builder().id(3L).name("tag3").build();
            TAG_TO_UPDATE_2 = Tag.builder().id(4L).name("tag4").build();
            TAG_TO_UPDATE_NEW = Tag.builder().name("tag5").build();
            LIST = Arrays.asList(TAG_TO_UPDATE_1, TAG_TO_UPDATE_2, TAG_TO_UPDATE_NEW);
        }

        @Test
        void testUpdateTagList() {
            when(tagDao.findByName(TAG_TO_UPDATE_1.getName())).thenReturn(Optional.of(TAG_TO_UPDATE_1));
            when(tagDao.findByName(TAG_TO_UPDATE_2.getName())).thenReturn(Optional.of(TAG_TO_UPDATE_2));
            when(tagDao.findByName(TAG_TO_UPDATE_NEW.getName())).thenReturn(Optional.empty());
            List<Tag> actual = service.updateTagList(LIST);
            List<Tag> empty = service.updateTagList(new ArrayList<>());
            assertTrue(empty.size() <= 0);
            assertEquals(LIST, actual);
        }
    }
}