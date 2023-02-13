package com.epam.esm.dao;

import com.epam.esm.entity.Tag;

import java.util.List;
import java.util.Optional;

public interface TagDao extends CRDDao<Tag> {

    Optional<Tag> getByName(String name);

    List<Tag> getTagsForCertificate(long certificateId);

    void assignTagToCertificate(long certificateId, long tagId);

    void detachTagsFromCertificate(long certificateId);
}
