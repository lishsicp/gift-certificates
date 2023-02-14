package com.epam.esm.service;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.filter.SearchFilter;

import java.util.List;

/**
 * This interface describes data access functionality for the {@link GiftCertificate} entity
 */
public interface GiftCertificateService extends CRUDService<GiftCertificate> {

    /**
     * Finds all {@link GiftCertificate} entities with specified parameters.
     * @param searchFilter object for search parameters
     * @return {@link List} of found {@link GiftCertificate}.
     */
    List<GiftCertificate> findAllCertificatesWithFilter(SearchFilter searchFilter);
}
