package com.epam.esm.dao;

import com.epam.esm.entity.GiftCertificate;
import org.springframework.data.domain.Pageable;
import org.springframework.util.MultiValueMap;

import java.util.List;
import java.util.Optional;

public interface GiftCertificateDao extends CRUDDao<GiftCertificate> {
    List<GiftCertificate> findAllWithFilter(Pageable page, MultiValueMap<String, String> params);

    Optional<GiftCertificate> findByName(String name);
}
