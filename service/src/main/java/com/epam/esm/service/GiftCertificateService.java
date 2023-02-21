package com.epam.esm.service;

import com.epam.esm.service.dto.GiftCertificateDto;
import org.springframework.data.domain.Page;
import org.springframework.util.MultiValueMap;

public interface GiftCertificateService extends CRUDService<GiftCertificateDto> {
    Page<GiftCertificateDto> getAllWithFilter(int page, int size, MultiValueMap<String, String> params);
}
