package com.epam.esm.service.impl;

import com.epam.esm.dao.TagDao;
import com.epam.esm.entity.Tag;
import com.epam.esm.service.TagService;
import com.epam.esm.service.exception.ErrorCodes;
import com.epam.esm.service.exception.PersistenceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class TagServiceImpl implements TagService {

    private final TagDao tagDao;

    @Autowired
    public TagServiceImpl(TagDao tagDao) {
        this.tagDao = tagDao;
    }

    @Override
    public List<Tag> getAll() {
        return tagDao.findAll();
    }

    @Override
    public Tag getById(long id) {
        var tag = tagDao.findById(id);
        if (tag.isEmpty()) {
            throw new PersistenceException(ErrorCodes.TAG_NOT_FOUND, id);
        }
        return tag.get();
    }

    @Override
    public Tag save(Tag tag) {
        var optionalTag = tagDao.findByName(tag.getName());
        if (optionalTag.isPresent()) {
            throw new PersistenceException(ErrorCodes.DUPLICATE_TAG, tag.getName());
        }
        var createTag = tagDao.create(tag);
        if (createTag.getId() == 0) {
            throw new PersistenceException(ErrorCodes.SAVE_FAILURE, tag.getName());
        }
        return createTag;
    }

    @Override
    public void delete(long id) {
        var tag = tagDao.findById(id);
        if (tag.isEmpty()) {
            throw new PersistenceException(ErrorCodes.TAG_NOT_FOUND, id);
        }
        tagDao.delete(id);
    }
}
