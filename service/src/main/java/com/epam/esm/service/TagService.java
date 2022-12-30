package com.epam.esm.service;

import com.epam.esm.dao.TagDao;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class TagService implements CRDService<Tag> {

    private final TagDao tagDao;

    @Autowired
    public TagService(TagDao tagDao) {
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
                        new ErrorBody(
                                String.format(ErrorMessage.RESOURCE_NOT_FOUND, id),
                                ErrorCode.TAG_NOT_FOUND
                        )
                ));
    }

    @Override
    public Tag save(Tag tag) {
        Optional<Tag> tagOptional = tagDao.getByName(tag.getName());
        if (tagOptional.isPresent()) {
            throw new DuplicatedKeyException(
                    new ErrorBody(
                            String.format(ErrorMessage.DUPLICATED_TAG, tag.getName()),
                            ErrorCode.DUPLICATION_KEY
                    )
            );
        }
        return tagDao.create(tag);
    }

    @Override
    public void delete(Long id) {
        tagDao.getById(id).ifPresentOrElse(t -> tagDao.remove(id), () -> {
            throw new ResourceNotFoundException(
                    new ErrorBody(
                            String.format(ErrorMessage.RESOURCE_NOT_FOUND, id),
                            ErrorCode.TAG_NOT_FOUND
                    )
            );
        });
    }
}
