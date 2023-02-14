package com.epam.esm.dao;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.filter.SearchFilter;

import java.util.List;

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
    List<GiftCertificate> getAll(SearchFilter searchFilter);
}
