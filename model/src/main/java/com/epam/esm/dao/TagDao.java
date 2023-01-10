package com.epam.esm.dao;

import com.epam.esm.entity.Tag;

import java.util.List;
import java.util.Optional;

public interface TagDao extends CRDDao<Tag> {

    Optional<Tag> getByName(String name);

    List<Tag> getTagsForCertificate(Long certificateId);

    void assignTagToCertificate(Long certificateId, Long tagId);

    void detachTagsFromCertificate(Long certificateId);
}
