package com.epam.esm.repository;

import com.epam.esm.entity.GiftCertificate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;

@Component
public interface CustomGiftCertificateRepository {
    Page<GiftCertificate> findAllWithParameters(MultiValueMap<String, String> params, Pageable pageable);
}
