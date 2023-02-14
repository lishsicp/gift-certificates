package com.epam.esm.dao;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;

import java.util.List;
import java.util.Optional;

/**
 * This interface describes abstract behavior and data access functionality for the {@link Tag} entity
 */
public interface TagDao extends CRDDao<Tag> {

    /**
     * Finds {@link Tag} by given name
     *
     * @param name the name of a {@link Tag}
     * @return Found {@link Tag} in an [Optional]
     */
    Optional<Tag> getByName(String name);

    /**
     * Finds {@link Tag} entities assigned for given {@link GiftCertificate}
     *
     * @param certificateId the {@link GiftCertificate} id
     * @return the {@link List} of {@link Tag} entities
     */
    List<Tag> getTagsForCertificate(long certificateId);

    /**
     * Assign {@link Tag} to {@link GiftCertificate}.
     *
     * @param certificateId the certificate id
     * @param tagId         the tag id
     */
    void assignTagToCertificate(long certificateId, long tagId);

    /**
     * Detaches all {@link Tag} entities from {@link GiftCertificate}.
     *
     * @param certificateId the certificate id
     */
    void detachTagsFromCertificate(long certificateId);
}
