package com.epam.esm.service.impl;

import com.epam.esm.dao.GiftCertificateDao;
import com.epam.esm.dao.TagDao;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.filter.SearchFilter;
import com.epam.esm.service.exception.PersistenceException;
import com.epam.esm.util.ModelFactory;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class GiftCertificateServiceImplTest {

    @Mock
    private GiftCertificateDao giftCertificateDao;

    @Mock
    private TagDao tagDao;

    @InjectMocks
    private GiftCertificateServiceImpl giftCertificateService;

    @Test
    void getAll_shouldReturnListOfCertificates() {
        var giftCertificate = ModelFactory.createGiftCertificate(1, 1);
        given(giftCertificateDao.findAll()).willReturn(List.of(giftCertificate));

        var actual = giftCertificateService.getAll();

        then(tagDao).should().findTagsForCertificate(anyLong());
        then(giftCertificateDao).should().findAll();
        assertEquals(List.of(giftCertificate), actual);
    }

    @Test
    void getAllCertificatesWithFilter_shouldReturnListOfCertificates() {
        SearchFilter searchFilterMock = mock(SearchFilter.class);
        var giftCertificate = ModelFactory.createGiftCertificate(1, 1);
        given(giftCertificateDao.findAll(searchFilterMock)).willReturn(List.of(giftCertificate));

        var actual = giftCertificateService.getAllCertificatesWithFilter(searchFilterMock);

        then(tagDao).should().findTagsForCertificate(anyLong());
        then(giftCertificateDao).should().findAll(searchFilterMock);
        assertEquals(List.of(giftCertificate), actual);
    }

    @Test
    void getById_shouldReturnCertificate() {
        var giftCertificate = ModelFactory.createGiftCertificate(1, 1);
        given(giftCertificateDao.findById(giftCertificate.getId())).willReturn(Optional.of(giftCertificate));

        GiftCertificate actual = giftCertificateService.getById(giftCertificate.getId());

        then(giftCertificateDao).should().findById(anyLong());
        assertEquals(giftCertificate, actual);
    }

    @Test
    void getById_shouldThrowException_whenCertificateNotFound() {
        assertThrows(PersistenceException.class, () -> giftCertificateService.getById(1));
    }

    @Nested
    class WhenSaving {

        @Test
        void save_shouldSaveCertificate() {
            var giftCertificate = ModelFactory.createGiftCertificate(1, 1, 0); // tag with id 0 mean this tag is new
            var tag1 = ModelFactory.createTag(1);
            given(giftCertificateDao.create(any())).willReturn(giftCertificate);
            given(tagDao.findByName(tag1.getName())).willReturn(Optional.of(tag1));

            giftCertificateService.save(giftCertificate);

            then(giftCertificateDao).should().create(any());
            then(tagDao).should().findTagsForCertificate(anyLong());
            then(tagDao).should().assignTagToCertificate(anyLong(), anyLong());
        }

        @Test
        void save_shouldThrowException_whenCertificateAlreadyExist() {
            var giftCertificate = ModelFactory.createGiftCertificate(1, 1);
            given(giftCertificateDao.findByName(anyString())).willReturn(Optional.of(giftCertificate));

            assertThrows(PersistenceException.class, () -> giftCertificateService.save(giftCertificate));
        }

        @Test
        void save_shouldThrowException_whenCertificateIdIsZero() {
            var giftCertificate = ModelFactory.createGiftCertificate(0);
            given(giftCertificateDao.create(any(GiftCertificate.class))).willReturn(giftCertificate);

            assertThrows(PersistenceException.class, () -> giftCertificateService.save(giftCertificate));
        }
    }

    @Nested
    class WhenDeleting {
        @Test
        void delete_shouldDeleteCertificate() {
            var giftCertificate = ModelFactory.createGiftCertificate();
            long id = giftCertificate.getId();
            given(giftCertificateDao.findById(anyLong())).willReturn(Optional.of(giftCertificate));

            giftCertificateService.delete(1);

            then(giftCertificateDao).should().delete(anyLong());
        }

        @Test
        void delete_shouldThrowException_whenCertificateNotFound() {
            assertThrows(PersistenceException.class, () -> giftCertificateService.delete(1));
        }
    }

    @Nested
    class WhenUpdating {
        @Test
        void update_shouldUpdateCertificate() {
            var tag1 = ModelFactory.createTag();
            var tag2 = ModelFactory.createTag();
            var giftCertificate = ModelFactory.createGiftCertificate(1, tag1.getId(), tag2.getId());
            given(giftCertificateDao.findById(anyLong())).willReturn(Optional.of(giftCertificate));
            given(tagDao.findByName(tag1.getName())).willReturn(Optional.of(tag1));
            given(tagDao.findByName(tag2.getName())).willReturn(Optional.of(tag2));
            given(tagDao.findTagsForCertificate(anyLong())).willReturn(ModelFactory.createTagList(1,2));

            giftCertificateService.update(1, giftCertificate);

            then(tagDao).should().detachTagsFromCertificate(anyLong());
            then(tagDao).should().findTagsForCertificate(anyLong());
            then(giftCertificateDao).should().update(any());
            then(tagDao).should(times(2)).assignTagToCertificate(anyLong(), anyLong());
        }
        
        @Test
        void update_shouldThrowException_whenCertificateDoNotExist() {
            given(giftCertificateDao.findById(anyLong())).willReturn(Optional.empty());
            var certificate = ModelFactory.createGiftCertificate();
            assertThrows(PersistenceException.class, () -> giftCertificateService.update(0, certificate));
        }
    }
}