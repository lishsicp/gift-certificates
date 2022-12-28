package com.epam.esm.service;

import com.epam.esm.dao.CRDDao;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.ErrorCode;
import com.epam.esm.exception.ErrorBody;
import com.epam.esm.exception.ErrorMessage;
import com.epam.esm.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class TagService implements CRDService<Tag> {

    private final CRDDao<Tag> tagDao;

    @Autowired
    public TagService(CRDDao<Tag> tagDao) {
        this.tagDao = tagDao;
    }

    @Override
    public List<Tag> findAll() {
        return tagDao.getAll();
    }

    @Override
    public Tag findById(Long id) {
        return tagDao.getById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        new ErrorBody(ErrorMessage.RESOURCE_NOT_FOUND, ErrorCode.TAG_NOT_FOUND, id)
                ));
    }

    @Override
    public Tag save(Tag tag) {
        return tagDao.create(tag);
    }

    @Override
    public void delete(Long id) {
        tagDao.getById(id).ifPresentOrElse(t -> tagDao.remove(id), () -> {
            throw new ResourceNotFoundException(
                    new ErrorBody(ErrorMessage.RESOURCE_NOT_FOUND, ErrorCode.TAG_NOT_FOUND, id)
            );
        });
        tagDao.remove(id);
    }
}
