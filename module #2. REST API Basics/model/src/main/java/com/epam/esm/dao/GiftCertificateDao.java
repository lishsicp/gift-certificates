package com.epam.esm.dao;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.filter.SearchFilter;

import java.util.List;
import java.util.Optional;

/**
 * This interface describes abstract behavior and data access functionality for the {@link GiftCertificate} entity
 */
public interface GiftCertificateDao extends CRUDDao<GiftCertificate> {

    /**
     * Finds all {@link GiftCertificate} entities with specified parameters.
     *
     * @param searchFilter object for search parameters
     * @return {@link List} of found {@link GiftCertificate}.
     */
    List<GiftCertificate> findAll(SearchFilter searchFilter);


    /**
     * Finds {@link GiftCertificate} by given name
     *
     * @param name the name of a {@link GiftCertificate}
     * @return Found {@link GiftCertificate} in an [Optional]
     */
    Optional<GiftCertificate> findByName(String name);
}
