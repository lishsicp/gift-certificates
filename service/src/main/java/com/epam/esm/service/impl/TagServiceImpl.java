package com.epam.esm.service.impl;

import com.epam.esm.dao.TagDao;
import com.epam.esm.entity.Tag;
import com.epam.esm.service.TagService;
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
    public List<Tag> findAll() {
        return tagDao.getAll();
    }

    @Override
    public Tag findById(long id) {
        return tagDao.getById(id);
    }

    @Override
    public Tag save(Tag tag) {
        return tagDao.create(tag);
    }

    @Override
    public void delete(long id) {
        tagDao.remove(id);
    }
}
