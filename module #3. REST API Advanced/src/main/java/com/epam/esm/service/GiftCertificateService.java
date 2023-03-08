package com.epam.esm.service;

import com.epam.esm.dto.GiftCertificateDto;
import org.springframework.data.domain.Page;
import org.springframework.util.MultiValueMap;

/**
 * This interface provides business logic for the {@link GiftCertificateDto} entity.
 */
public interface GiftCertificateService extends CRUDService<GiftCertificateDto> {

    /**
     * Retrieves a paginated list of {@link GiftCertificateDto} entities that match the specified filter parameters.
     *
     * @param page   the page number of the result set to retrieve
     * @param size   the number of items per page to retrieve
     * @param params a {@link MultiValueMap} containing key-value pairs that define the search criteria
     * @return a {@link Page} object containing the requested list of {@link GiftCertificateDto} objects
     */
    Page<GiftCertificateDto> getAllWithFilter(int page, int size, MultiValueMap<String, String> params);
}


