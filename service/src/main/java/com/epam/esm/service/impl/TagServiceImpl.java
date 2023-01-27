package com.epam.esm.service.impl;

import com.epam.esm.dao.TagDao;
import com.epam.esm.entity.Tag;
import com.epam.esm.service.GenericService;
import com.epam.esm.service.TagService;
import com.epam.esm.service.exception.ExceptionErrorCode;
import com.epam.esm.service.exception.PersistentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TagServiceImpl extends GenericService<Tag> implements TagService {

    private final TagDao tagDao;

    @Autowired
    public TagServiceImpl(TagDao tagDao) {
        super(tagDao);
        this.tagDao = tagDao;
    }

    @Override
    public Tag save(Tag tag) throws PersistentException {
        Optional<Tag> optionalTag = tagDao.findByName(tag.getName());
        if (optionalTag.isPresent())
            throw new PersistentException(ExceptionErrorCode.DUPLICATED_TAG, tag.getName());
        return tagDao.save(tag);
    }
}
