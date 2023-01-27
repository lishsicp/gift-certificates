package com.epam.esm.service.impl;

import com.epam.esm.dao.TagDao;
import com.epam.esm.entity.Tag;
import com.epam.esm.service.TagService;
import com.epam.esm.service.exception.ExceptionErrorCode;
import com.epam.esm.service.exception.PersistentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TagServiceImpl implements TagService {

    private final TagDao tagDao;

    @Autowired
    public TagServiceImpl(TagDao tagDao) {
        this.tagDao = tagDao;
    }

    @Override
    public List<Tag> getAll(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        return tagDao.findAll(pageable);
    }

    @Override
    public Tag getById(Long id) throws PersistentException {
        return tagDao.findById(id).orElseThrow(() -> new PersistentException(ExceptionErrorCode.TAG_NOT_FOUND, id));
    }

    @Override
    public Tag save(Tag tag) throws PersistentException {
        Optional<Tag> optionalTag = tagDao.getByName(tag.getName());
        if (optionalTag.isPresent())
            throw new PersistentException(ExceptionErrorCode.DUPLICATED_TAG, tag.getName());
        return tagDao.save(tag);
    }

    @Override
    public void delete(Long id) throws PersistentException {
        Tag tag = tagDao.findById(id).orElseThrow(() -> new PersistentException(ExceptionErrorCode.TAG_NOT_FOUND, id));
        tagDao.delete(tag.getId());
    }
}
