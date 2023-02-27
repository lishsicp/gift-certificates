package com.epam.esm.dao.impl;

import com.epam.esm.config.H2Config;
import com.epam.esm.dao.TagDao;
import com.epam.esm.entity.Tag;
import com.epam.esm.util.ModelFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = H2Config.class)
@ActiveProfiles("test")
@Transactional
class TagDaoImplTest {

    @Autowired
    private TagDao tagDao;

    @Test
    void create_shouldCreateTagAndGenerateId() {
        Tag tag = ModelFactory.createTag();
        Tag createdTag = tagDao.create(tag);
        assertTrue(createdTag.getId() > 0);
    }

    @Test
    void findAll_shouldReturnAllTags() {
        List<Tag> expected = ModelFactory.createTagList(1,2,3);
        List<Tag> tags = tagDao.findAll();
        assertEquals(expected, tags);
    }

    @Test
    void findById_shouldReturnTag() {
        long id = 1;
        var expected = Optional.of(ModelFactory.createTag(id));
        Optional<Tag> actual = tagDao.findById(id);
        assertTrue(actual.isPresent());
        assertEquals(expected, actual);
    }

    @Test
    void findByName_shouldReturnTag() {
        long id = 1;
        var expected = Optional.of(ModelFactory.createTag(id));

        Optional<Tag> actual = tagDao.findByName(expected.get().getName());

        assertTrue(actual.isPresent());
        assertEquals(expected, actual);
    }

    @Test
    void delete_shouldDeleteTag() {
        long createTagId = tagDao.create(ModelFactory.createTag()).getId();

        tagDao.delete(createTagId);

        assertTrue(tagDao.findById(createTagId).isEmpty());
    }

    @Test
    void findTagsForCertificate_shouldReturnAssignedTags() {
        var excepted = ModelFactory.createTagList(2, 3);
        List<Tag> tagsRefToCertificate = tagDao.findTagsForCertificate(2);
        assertIterableEquals(excepted, tagsRefToCertificate);
    }

    @Test
    void assignTagToCertificate_shouldReturnAllAssignedTags() {
        Tag tag = tagDao.create(ModelFactory.createTag());
        var expected = ModelFactory.createTagList(1, 2, 3);
        expected.add(tag);
        tagDao.assignTagToCertificate(1, tag.getId());

        assertEquals(expected, tagDao.findTagsForCertificate(1));
    }

    @Test
    void detachTagsFromCertificate_shouldReturnEmptyTagList() {
        long tagId = tagDao.create(ModelFactory.createTag()).getId();
        tagDao.assignTagToCertificate(1, tagId);

        tagDao.detachTagsFromCertificate(1);

        assertTrue(tagDao.findTagsForCertificate(1).isEmpty());
    }
}