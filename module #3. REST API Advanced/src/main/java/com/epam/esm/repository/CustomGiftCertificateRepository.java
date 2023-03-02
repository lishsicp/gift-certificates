package com.epam.esm.repository;

import com.epam.esm.entity.GiftCertificate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;

/**
 * This interface provides custom data access functionality for the GiftCertificate entity. It defines a method for
 * <p>
 * finding all GiftCertificate entities that match the specified filter parameters.
 * <p>
 */
@Component
public interface CustomGiftCertificateRepository {

    /**
     * Finds all GiftCertificate entities that match the specified filter parameters.
     *
     * @param params   a MultiValueMap containing key-value pairs that define the search criteria
     * @param pageable a Pageable object that specifies the requested page and page size for the result set
     * @return a Page object containing the requested list of GiftCertificate objects
     */
    Page<GiftCertificate> findAllWithParameters(MultiValueMap<String, String> params, Pageable pageable);
}
