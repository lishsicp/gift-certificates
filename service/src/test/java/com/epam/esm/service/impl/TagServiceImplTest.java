package com.epam.esm.service.impl;

import com.epam.esm.dao.TagDao;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.DaoException;
import com.epam.esm.service.TagService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class TagServiceImplTest {

    TagService tagService;
    TagDao tagDao;
    Tag tag;

    @BeforeEach
    public void setUp() {
        tagDao = mock(TagDao.class);
        tagService = new TagServiceImpl(tagDao);
        tag = new Tag();
        tag.setId(1L);
        tag.setName("tagTestName");
    }

    @Test
    void save_shouldSaveTag() {
        when(tagDao.create(any())).thenReturn(tag);
        Tag actual = tagService.save(tag);
        verify(tagDao).create(any());
        assertEquals(tag, actual);
    }

    @Test
    void findAll_shouldReturnListOfTags() {
        when(tagDao.getAll()).thenReturn(List.of(tag));
        var actual = tagService.findAll();
        assertEquals(List.of(tag), actual);
        verify(tagDao).getAll();
    }

    @Test
    void findById_shouldReturnTag() {
        when(tagDao.getById(anyLong())).thenReturn(tag);
        Tag tagById = tagService.findById(1L);
        assertTrue(tagById.getId() > 0);
        assertEquals("tagTestName", tagById.getName());
        verify(tagDao).getById(anyLong());
    }

    @Test
    void findById_shouldThrowException_whenTagNotFound() {
        when(tagDao.getById(anyLong())).thenThrow(DaoException.class);
        assertThrows(DaoException.class, () -> tagService.findById(1L));
    }


    @Test
    void delete_shouldDeleteTag() throws DaoException {
        when(tagDao.getById(1L)).thenReturn(tag);
        tagService.delete(1L);
        verify(tagDao).remove(1L);
    }

    @Test
    void delete_shouldThrowException_whenTagNotFound() throws DaoException {
        doThrow(DaoException.class).when(tagDao).remove(anyLong());
        assertThrows(DaoException.class, () -> tagService.delete(1L));
    }

}