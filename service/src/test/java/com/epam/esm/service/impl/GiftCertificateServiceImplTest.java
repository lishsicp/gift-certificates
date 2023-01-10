package com.epam.esm.service.impl;

import com.epam.esm.dao.GiftCertificateDao;
import com.epam.esm.dao.TagDao;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.entity.filter.SearchFilter;
import com.epam.esm.exception.DaoException;
import com.epam.esm.exception.DaoExceptionErrorCode;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.service.exception.IncorrectUpdateValueException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GiftCertificateServiceImplTest {

    private final static int EMPTY_CERTIFICATE_ERROR = 40009;

    GiftCertificateService giftCertificateService;

    GiftCertificateDao giftCertificateDao;
    TagDao tagDao;
    Tag tag;
    GiftCertificate giftCertificate;

    {
        tag = new Tag();
        tag.setId(1L);
        tag.setName("tagTestName");

        giftCertificate = new GiftCertificate();
        giftCertificate.setId(1L);
        giftCertificate.setName("testGift");
        giftCertificate.setDescription("test gift cert");
        giftCertificate.setDuration(30);
        giftCertificate.setPrice(new BigDecimal(1));
        giftCertificate.setCreateDate(LocalDateTime.now());
        giftCertificate.setLastUpdateDate(LocalDateTime.now());
        giftCertificate.setTags(List.of(tag));
    }

    @BeforeEach
    public void setUp() {
        giftCertificateDao = mock(GiftCertificateDao.class);
        tagDao = mock(TagDao.class);
        giftCertificateService = new GiftCertificateServiceImpl(tagDao, giftCertificateDao);
    }

    @Test
    void findAll_giftCertificateDaoInvoke() {
        when(giftCertificateDao.getAll()).thenReturn(List.of(giftCertificate));
        giftCertificateService.findAll();
        verify(tagDao).getTagsForCertificate(anyLong());
        verify(giftCertificateDao).getAll();
    }

    @Test
    void findAllCertificatesWithFilter_giftCertificateDaoInvoked_WithFilterMock() {
        SearchFilter searchFilterMock = mock(SearchFilter.class);
        when(giftCertificateDao.getAll(searchFilterMock)).thenReturn(List.of(giftCertificate));
        giftCertificateService.findAllCertificatesWithFilter(searchFilterMock);
        verify(tagDao).getTagsForCertificate(anyLong());
        verify(giftCertificateDao).getAll(searchFilterMock);
    }

    @Test
    void findById_ReturnsCertificate() throws DaoException {
        when(giftCertificateDao.getById(giftCertificate.getId())).thenReturn(giftCertificate);
        GiftCertificate actual = giftCertificateService.findById(giftCertificate.getId());
        assertEquals(giftCertificate, actual);
    }

    @Test
    void save_CorrectCertificate() throws DaoException {
        when(giftCertificateDao.create(any())).thenReturn(giftCertificate);
        when(tagDao.getAll()).thenReturn(giftCertificate.getTags());
        when(tagDao.getByName(any())).thenReturn(Optional.of(tag));
        giftCertificateService.save(giftCertificate);
        verify(giftCertificateDao).create(any());
        verify(tagDao).getTagsForCertificate(anyLong());
        verify(tagDao).assignTagToCertificate(anyLong(), anyLong());
    }

    @Test
    void update_EmptyCertificate_ThrowsException() {
        var ex = assertThrows(IncorrectUpdateValueException.class, () -> giftCertificateService.update(new GiftCertificate()));
        assertEquals(EMPTY_CERTIFICATE_ERROR, ex.getErrorCode());
    }

    @Test
    void delete_giftCertificateDaoRemoveInvoke() throws DaoException {
        giftCertificateService.delete(1L);
        verify(giftCertificateDao).remove(1L);
    }

    @Test
    void update_UpdatesCertificate() throws DaoException, IncorrectUpdateValueException {
        when(tagDao.getByName(any())).thenReturn(Optional.of(tag));
        giftCertificateService.update(giftCertificate);
        verify(tagDao).detachTagsFromCertificate(anyLong());
        verify(tagDao).getTagsForCertificate(anyLong());
        verify(giftCertificateDao).update(any());
        verify(tagDao).assignTagToCertificate(anyLong(), anyLong());
    }
}