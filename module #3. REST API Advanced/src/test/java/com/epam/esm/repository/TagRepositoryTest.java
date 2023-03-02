package com.epam.esm.repository;
import com.epam.esm.entity.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@ActiveProfiles("test")
@DataJpaTest
class TagRepositoryTest {

    private static final Tag TAG_1 = Tag.builder().id(1L).name("tag1").build();
    private static final Tag TAG_2 = Tag.builder().id(2L).name("tag2").build();
    private static final Tag TAG_3 = Tag.builder().id(3L).name("tag3").build();
    private static final Tag TAG_4 = Tag.builder().id(4L).name("tag4").build();
    private static final Tag TAG_5 = Tag.builder().id(5L).name("tag5").build();

    private static final Tag MOST_POPULAR_TAG = Tag.builder().id(2L).name("tag2").build();

    private static final Pageable PAGE_REQUEST = PageRequest.of(0, 5);
    private static final Long NON_EXISTED_TAG_ID = 999L;

    @Autowired
    private TagRepository repository;

    @Test
    void findAll_shouldReturnAll_whenValidPageRequest() {
        Page<Tag> tags = repository.findAll(PAGE_REQUEST);
        List<Tag> expected = Arrays.asList(TAG_1,TAG_2,TAG_3,TAG_4,TAG_5);
        assertEquals(expected, tags.getContent());
    }

    @Test
    void findById_shouldReturnOne_whenExistingId() {
        Optional<Tag> tag = repository.findById(TAG_1.getId());
        assertTrue(tag.isPresent());
        assertEquals(TAG_1, tag.get());
    }

    @Test
    void findById_shouldReturnEmptyOptional_whenNonexistentId() {
        Optional<Tag> tag = repository.findById(NON_EXISTED_TAG_ID);
        assertTrue(tag.isEmpty());
    }

    @Test
    void findByName_shouldReturnOptionalOfTag_whenExistingName() {
        Optional<Tag> tagOptional = repository.findTagByName(TAG_1.getName());
        assertTrue(tagOptional.isPresent());
    }

    @Test
    void findByName_shouldReturnEmptyOptional_whenNonexistentName() {
        assertTrue(repository.findTagByName(TAG_1.getName() + "POSTFIX").isEmpty());
    }

    @Test
    void delete_shouldDeleteTag_whenExistingId() {
        Tag testTag = new Tag();
        testTag.setName("testNameDelete");
        Tag tagWithId = repository.save(testTag);
        repository.delete(tagWithId);
        assertTrue(repository.findById(tagWithId.getId()).isEmpty());
    }

    @Test
    void save_shouldSaveAndGenerateId() {
        Tag tag = new Tag();
        tag.setName("testTagName");
        Tag savedTag = repository.save(tag);
        assertTrue(savedTag.getId() > 0);
    }

    @Test
    void findMostWidelyUsedTagWithHighestCostOfAllOrders_shouldReturnMostPopularTag() {
        Optional<Tag> expected = Optional.of(MOST_POPULAR_TAG);
        Optional<Tag> actual = repository.findMostWidelyUsedTagWithHighestCostOfAllOrders();
        assertEquals(expected, actual);
    }
}