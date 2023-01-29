package com.epam.esm.dao.impl;

import com.epam.esm.config.TestDaoConfig;
import com.epam.esm.dao.TagDao;
import com.epam.esm.entity.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.ConfigDataApplicationContextInitializer;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import javax.transaction.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = TestDaoConfig.class)
@ContextConfiguration(initializers = ConfigDataApplicationContextInitializer.class)
@ActiveProfiles("test")
@Transactional
class TagDaoImplTest {

    Tag TAG_1 = Tag.builder().id(1L).name("tag1").build();
    Tag TAG_2 = Tag.builder().id(2L).name("tag2").build();
    Tag TAG_3 = Tag.builder().id(3L).name("tag3").build();
    Tag TAG_4 = Tag.builder().id(4L).name("tag4").build();
    Tag TAG_5 = Tag.builder().id(5L).name("tag5").build();

    Tag MOST_POPULAR_TAG = Tag.builder().id(2L).name("tag2").build();
    Pageable PAGE_REQUEST = PageRequest.of(0, 5);

    @Autowired
    TagDao tagDao;

    @Test
    void testFindAll() {
        List<Tag> tags = tagDao.findAll(PAGE_REQUEST);
        List<Tag> expected = Arrays.asList(TAG_1,TAG_2,TAG_3,TAG_4,TAG_5);
        assertIterableEquals(expected, tags);
    }

    @Test
    void testFindById() {
        Optional<Tag> tag = tagDao.findById(TAG_1.getId());
        assertTrue(tag.isPresent());
        assertEquals(TAG_1, tag.get());
    }

    @Test
    void getByName_ReturnsTagWithName() {
        Optional<Tag> tagOptional = tagDao.findByName(TAG_1.getName());
        assertTrue(tagOptional.isPresent());
    }

    @Test
    void testDelete() {
        Tag testTag = new Tag();
        testTag.setName("testNameDelete");
        Tag tagWithId = tagDao.save(testTag);
        tagDao.delete(tagWithId.getId());
        assertTrue(tagDao.findById(tagWithId.getId()).isEmpty());
    }

    @Test
    void testSave() {
        Tag tag = new Tag();
        tag.setName("testTagName");
        assertTrue(tagDao.save(tag).getId() > 0);
    }

    @Test
    void findMostWidelyUsedTagWithHighestCostOfAllOrders() {
        Optional<Tag> expected = Optional.of(MOST_POPULAR_TAG);
        Optional<Tag> actual = tagDao.findMostWidelyUsedTagWithHighestCostOfAllOrders();
        assertEquals(expected, actual);
    }
}