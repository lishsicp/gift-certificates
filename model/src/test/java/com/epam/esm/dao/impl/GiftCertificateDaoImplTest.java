package com.epam.esm.dao.impl;

import com.epam.esm.config.H2Config;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.filter.SearchFilter;
import com.epam.esm.exception.DaoException;
import com.epam.esm.exception.DaoExceptionErrorCode;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = H2Config.class)
@ActiveProfiles("test")
class GiftCertificateDaoImplTest {

    @Autowired
    GiftCertificateDaoImpl giftCertificateDao;

    @Test
    void create_ReturnsCreatedCertificateId() throws DaoException {
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
    void getAllWithFilterPassed_ReturnsCertificatesWithFilterApplied() {
        SearchFilter searchFilter = new SearchFilter();
        searchFilter.setTagName("tag1");
        searchFilter.setOrderBy("NAME");
        searchFilter.setOrderByType("DESC");
        List<GiftCertificate> giftCertificateList = giftCertificateDao.getAll(searchFilter);
        long actualAmount = giftCertificateList.size();
        long checkedAmount = giftCertificateList.stream()
                .filter(c -> c.getName().contains("gift"))
                .filter(c -> c.getDescription().contains("description"))
                .count();

        assertEquals(checkedAmount, actualAmount);
    }

    @Test
    void getAll_ReturnsNotEmptyList() {
        List<GiftCertificate> giftCertificateList = giftCertificateDao.getAll();
        assertTrue(giftCertificateList.size() > 0);
    }

    @Test
    void getById_ReturnsCertificateWithId() throws DaoException {
        GiftCertificate giftCertificate = giftCertificateDao.getById(1L);

        assertEquals(1L, giftCertificate.getId());
    }

    @Test
    void update_UpdatesOnlyCertificatesName() throws DaoException {
        GiftCertificate initialCertificate = new GiftCertificate();
        initialCertificate.setName("initialName");
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
    void remove_CertificateNotPresent_ThrowsDaoExceptionWithCertificateNotFoundErrorCode() throws DaoException {
        GiftCertificate initialCertificate = new GiftCertificate();
        initialCertificate.setName("testDelete");
        initialCertificate.setDescription("testDelete");
        initialCertificate.setPrice(BigDecimal.ONE);
        initialCertificate.setDuration(2);
        initialCertificate.setCreateDate(LocalDateTime.now());
        Long id = giftCertificateDao.create(initialCertificate).getId();

        giftCertificateDao.remove(id);

        Executable getByIdExec = () -> giftCertificateDao.getById(id);
        DaoException ex = assertThrows(DaoException.class, getByIdExec);
        assertThrows(DaoException.class,() -> giftCertificateDao.getById(id));
        assertEquals(DaoExceptionErrorCode.CERTIFICATE_NOT_FOUND, ex.getErrorCode());
    }
}