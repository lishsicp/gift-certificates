package com.epam.esm.service.impl;

import com.epam.esm.dao.TagDao;
import com.epam.esm.entity.Tag;
import com.epam.esm.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TagServiceImpl implements TagService {

    private final TagDao tagDao;

    @Autowired
    public TagServiceImpl(TagDao tagDao) {
        this.tagDao = tagDao;
    }

    @Override
    public List<Tag> findAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return tagDao.getAll(pageable);
    }

    @Override
    public Tag findById(Long id) {
        return tagDao.getById(id);
    }

    @Override
    public Tag save(Tag tag) {
        return tagDao.create(tag);
    }

    @Override
    public void delete(Long id) {
        tagDao.delete(tagDao.getById(id));
    }
}
