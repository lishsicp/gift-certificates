package com.epam.esm.service.impl;

import com.epam.esm.dao.TagDao;
import com.epam.esm.entity.Tag;
import com.epam.esm.service.exception.PersistenceException;
import com.epam.esm.util.ModelFactory;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.anyLong;

@ExtendWith(MockitoExtension.class)
class TagServiceImplTest {

    @Mock
    TagDao tagDao;

    @InjectMocks
    TagServiceImpl tagService;

    @Test
    void getAll_shouldReturnListOfTags() {
        var tagList = ModelFactory.createTagList(1, 2, 3);
        given(tagDao.findAll()).willReturn(tagList);

        var actual = tagService.getAll();

        then(tagDao).should().findAll();
        assertEquals(tagList, actual);
    }

    @Test
    void getById_shouldReturnTag() {
        var tag = ModelFactory.createTag();
        given(tagDao.findById(anyLong())).willReturn(Optional.of(tag));

        Tag tagById = tagService.getById(tag.getId());

        then(tagDao).should().findById(anyLong());
        assertEquals(tag, tagById);
    }

    @Test
    void getById_shouldThrowException_whenTagNotFound() {
        given(tagDao.findById(anyLong())).willReturn(Optional.empty());

        assertThrows(PersistenceException.class, () -> tagService.getById(1));
    }

    @Nested
    class whenSave {

        @Test
        void save_shouldSaveTag() {
            var expected = ModelFactory.createTag();
            given(tagDao.create(any())).willReturn(expected);

            Tag actual = tagService.save(expected);

            then(tagDao).should().create(any());
            assertEquals(expected, actual);
        }

        @Test
        void save_shouldThrowException_whenTagExists() {
            var expected = ModelFactory.createTag();
            given(tagDao.findByName(anyString())).willReturn(Optional.of(expected));

            assertThrows(PersistenceException.class, () -> tagService.save(expected));
        }

        @Test
        void save_shouldThrowException_whenTagIdIsZero() {
            var expected = ModelFactory.createTag(0);
            given(tagDao.create(any(Tag.class))).willReturn(expected);

            assertThrows(PersistenceException.class, () -> tagService.save(expected));
        }
    }

    @Nested
    class whenDelete {

        @Test
        void delete_shouldDeleteTag() {
            var tag = ModelFactory.createTag();
            long id = tag.getId();
            given(tagDao.findById(anyLong())).willReturn(Optional.of(tag));

            tagService.delete(id);

            then(tagDao).should().delete(id);
        }

        @Test
        void delete_shouldThrowException_whenTagNotFound() {
            assertThrows(PersistenceException.class, () -> tagService.delete(1));
        }
    }

}