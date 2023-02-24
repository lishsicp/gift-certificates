package com.epam.esm.service;

import com.epam.esm.service.dto.GiftCertificateDto;
import org.springframework.data.domain.Page;
import org.springframework.util.MultiValueMap;

/**
 * This interface provides data access functionality for the GiftCertificate entity. It extends the CRUDService
 * <p>
 * interface and adds a method for finding all GiftCertificate entities with specified parameters.
 */
public interface GiftCertificateService extends CRUDService<GiftCertificateDto> {

    /**
     * Retrieves a paginated list of GiftCertificate entities that match the specified filter parameters.
     *
     * @param page   the page number of the result set to retrieve
     * @param size   the number of items per page to retrieve
     * @param params a MultiValueMap containing key-value pairs that define the search criteria
     * @return a Page object containing the requested list of GiftCertificateDto objects
     */
    Page<GiftCertificateDto> getAllWithFilter(int page, int size, MultiValueMap<String, String> params);
}


