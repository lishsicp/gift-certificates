package com.epam.esm.service;

import com.epam.esm.entity.GiftCertificate;
import org.springframework.util.MultiValueMap;

import java.util.List;

public interface GiftCertificateService extends CRUDService<GiftCertificate> {
    List<GiftCertificate> findAllWithFilter(int page, int size, MultiValueMap<String, String> params);
}
