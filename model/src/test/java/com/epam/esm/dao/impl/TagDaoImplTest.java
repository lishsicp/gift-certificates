package com.epam.esm.dao.impl;

import com.epam.esm.config.H2Config;
import com.epam.esm.dao.TagDao;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.DaoException;
import com.epam.esm.exception.DaoExceptionErrorCode;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = H2Config.class)
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.DisplayName.class)
class TagDaoImplTest {

    @Autowired
    TagDao tagDao;

    @Test
    void create_ReturnsCreatedTagId() throws DaoException {
        Tag tag = new Tag();
        tag.setName("testTagName");
        assertTrue(tagDao.create(tag).getId() > 0);
    }

    @Test
    void getAll_ReturnsMoreThanZero() {
        List<Tag> tags = tagDao.getAll();
        assertTrue(tags.size() > 0);
    }

    @Test
    void getById_ReturnsTagWithId() throws DaoException {
        Tag tag = tagDao.getById(1L);
        assertEquals(1L, tag.getId());
    }

    @Test
    void getByName_ReturnsTagWithName() {
        Optional<Tag> tagOptional = tagDao.getByName("tag1");
        assertEquals("tag1", tagOptional.get().getName());
    }

    @Test
    void remove_TagNotPresent_ThrowsDaoExceptionWithTagNotFoundErrorCode() throws DaoException {
        Tag testTag = new Tag();
        testTag.setName("testNameDelete");
        Tag tagWithId = tagDao.create(testTag);

        tagDao.remove(tagWithId.getId());
        Executable getByIdExec = () -> tagDao.getById(tagWithId.getId());
        DaoException ex = assertThrows(DaoException.class, getByIdExec);
        assertEquals(DaoExceptionErrorCode.TAG_NOT_FOUND, ex.getErrorCode());
    }

    @Test
    void getTagsForCertificate_ReturnsAssignedTags() {
        List<Tag> tagsRefToCertificate = tagDao.getTagsForCertificate(2L);
        List<Long> expectedTagIdList = tagsRefToCertificate.stream()
                .map(Tag::getId)
                .collect(Collectors.toList());

        assertEquals(List.of(2L, 3L), expectedTagIdList);
    }

    @Test
    void assignTagToCertificate_NotEmptyTagList() throws DaoException {
        Tag tag = new Tag();
        tag.setName("testAssign");
        Long tagId = tagDao.create(tag).getId();
        tagDao.assignTagToCertificate(1L, tagId);

        assertEquals(tagDao.getAll().size(), tagDao.getTagsForCertificate(1L).size());
    }

    @Test
    void detachTagsFromCertificate_EmptyTagList() throws DaoException {
        Tag tag = new Tag();
        tag.setName("testDetach");
        Long tagId = tagDao.create(tag).getId();
        tagDao.assignTagToCertificate(1L, tagId);
        tagDao.detachTagsFromCertificate(1L);
        assertTrue(tagDao.getTagsForCertificate(1L).isEmpty());
    }
}