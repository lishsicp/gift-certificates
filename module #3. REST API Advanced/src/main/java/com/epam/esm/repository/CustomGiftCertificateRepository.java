package com.epam.esm.repository;

import com.epam.esm.entity.GiftCertificate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.util.MultiValueMap;

/**
 * This interface provides custom data access functionality for the {@link GiftCertificate} entity.
 */
public interface CustomGiftCertificateRepository {

    /**
     * Finds all {@link GiftCertificate} entities that match the specified filter parameters.
     *
     * @param params   a {@link MultiValueMap} containing key-value pairs that define the search criteria
     * @param pageable a {@link Pageable} object that specifies the requested page and page size for the result set
     * @return a {@link Page} object containing the requested list of GiftCertificate objects
     */
    Page<GiftCertificate> findAllWithParameters(MultiValueMap<String, String> params, Pageable pageable);
}
