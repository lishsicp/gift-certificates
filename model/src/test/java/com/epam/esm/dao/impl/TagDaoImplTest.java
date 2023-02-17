package com.epam.esm.dao.impl;

import com.epam.esm.config.H2Config;
import com.epam.esm.dao.TagDao;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.DaoException;
import com.epam.esm.exception.ErrorCodes;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = H2Config.class)
@ActiveProfiles("test")
@Transactional
class TagDaoImplTest {

    @Autowired
    TagDao tagDao;

    Tag tag1;
    Tag tag2;
    Tag tag3;
    List<Tag> tagList;

    @BeforeEach
    void setUp() {
        tag1 = new Tag(1, "tag1");
        tag2 = new Tag(2, "tag2");
        tag3 = new Tag(3, "tag3");
        tagList = List.of(tag1, tag2, tag3);
    }

    @Test
    void create_ShouldCreateTagAndGenerateId() {
        Tag tag = new Tag();
        tag.setName("testTagName");
        assertTrue(tagDao.create(tag).getId() > 0);
    }

    @Test
    void getAll_ShouldReturnAllTags() {
        List<Tag> tags = tagDao.getAll();
        assertEquals(tagList, tags);
    }

    @Test
    void getById_ShouldReturnTag() {
        Tag tag = tagDao.getById(tag1.getId());
        assertEquals(tag1, tag);
    }

    @Test
    void getById_shouldReturnTag_whenTagWithNameExists() {
        Optional<Tag> tagOptional = tagDao.getByName(tag1.getName());
        assertTrue(tagOptional.isPresent());
        assertEquals(Optional.of(tag1), tagOptional);
    }

    @Test
    void remove_shouldThrowException_whenTagHasBeenRemoved() {
        Tag testTag = new Tag();
        testTag.setName("testNameDelete");
        Tag tagWithId = tagDao.create(testTag);
        assertNotNull(tagDao.getById(tagWithId.getId()));
        tagDao.remove(tagWithId.getId());
        Executable getByIdExec = () -> tagDao.getById(tagWithId.getId());
        assertThrows(DaoException.class, getByIdExec);
    }

    @Test
    void getTagsForCertificate_ShouldReturnAssignedTags() {
        List<Tag> tagsRefToCertificate = tagDao.getTagsForCertificate(2L);
        assertEquals(List.of(tag2, tag3), tagsRefToCertificate);
    }

    @Test
    void assignTagToCertificate_ShouldReturnAllTags() {
        Tag tag = new Tag();
        tag.setName("testAssign");
        long tagId = tagDao.create(tag).getId();
        tagDao.assignTagToCertificate(1L, tagId);
        assertEquals(tagDao.getAll(), tagDao.getTagsForCertificate(1L));
    }

    @Test
    void detachTagsFromCertificate_ShouldReturnEmptyTagList() {
        Tag tag = new Tag();
        tag.setName("testDetach");
        long tagId = tagDao.create(tag).getId();
        tagDao.assignTagToCertificate(1L, tagId);
        tagDao.detachTagsFromCertificate(1L);
        assertTrue(tagDao.getTagsForCertificate(1L).isEmpty());
    }
}