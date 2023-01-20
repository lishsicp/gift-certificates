package com.epam.esm.dao.impl;

import com.epam.esm.dao.GenericDao;
import com.epam.esm.dao.TagDao;
import com.epam.esm.entity.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;


@Repository
public class TagDaoImpl extends GenericDao<Tag> implements TagDao {

    @Autowired
    public TagDaoImpl() {
        super(Tag.class);
    }

}
