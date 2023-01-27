package com.epam.esm.dao;

import com.epam.esm.entity.Tag;

import java.util.Optional;

public interface TagDao extends CRDDao<Tag> {
    Optional<Tag> findByName(String name);
}
