package com.epam.esm.service;

import com.epam.esm.entity.GiftCertificate;
import org.springframework.data.domain.Page;
import org.springframework.util.MultiValueMap;

public interface GiftCertificateService extends CRUDService<GiftCertificate> {
    Page<GiftCertificate> getAllWithFilter(int page, int size, MultiValueMap<String, String> params);
}
