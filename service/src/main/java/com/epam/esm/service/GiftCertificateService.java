package com.epam.esm.service;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.filter.SearchFilter;

import java.util.List;

public interface GiftCertificateService extends CRUDService<GiftCertificate> {

    List<GiftCertificate> findAllCertificatesWithFilter(SearchFilter searchFilter);
}
