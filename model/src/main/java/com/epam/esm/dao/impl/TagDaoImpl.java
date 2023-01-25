package com.epam.esm.dao.impl;

import com.epam.esm.dao.GenericDao;
import com.epam.esm.dao.TagDao;
import com.epam.esm.entity.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public class TagDaoImpl extends GenericDao<Tag> implements TagDao {

    @Autowired
    public TagDaoImpl() {
        super(Tag.class);
    }

    @Override
    public Optional<Tag> getByName(String name) {
        return entityManager
                .createQuery("select tag from Tag tag where tag.name = :name", Tag.class)
                .setParameter("name", name)
                .getResultList().stream().findFirst();
    }
}
