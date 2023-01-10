package com.epam.esm.dao;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.filter.SearchFilter;

import java.util.List;

public interface GiftCertificateDao extends CRUDDao<GiftCertificate> {
    List<GiftCertificate> getAll(SearchFilter searchFilter);

}
