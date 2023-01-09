package com.epam.esm.service.impl;

import com.epam.esm.dao.TagDao;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.DaoException;
import com.epam.esm.service.TagService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class TagServiceImplTest {

    TagService tagService;
    TagDao tagDao;
    Tag tag;

    {
        tag = new Tag();
        tag.setId(1L);
        tag.setName("tagTestName");
    }

    @BeforeEach
    public void setUp() {
        tagDao = mock(TagDao.class);
        tagService = new TagServiceImpl(tagDao);
    }

    @Test
    void save_tagDaoCreateInvoked() throws DaoException {
        tagService.save(tag);

        verify(tagDao).create(any());
    }

    @Test
    void findAll_tagDaoFindAllInvoked() {
        tagService.findAll();
        verify(tagDao).getAll();
    }

    @Test
    void findById_ReturnsTagWithIdWhenGetTagById() throws DaoException {
        when(tagDao.getById(any())).thenReturn(tag);

        Tag tagById = tagService.findById(1L);

        assertTrue(tagById.getId() > 0);
        assertEquals("tagTestName", tagById.getName());
    }

    @Test
    void findById_ThrowsException() throws DaoException {
        when(tagDao.getById(1L)).thenThrow(DaoException.class);
        assertThrows(DaoException.class, () -> tagService.findById(1L));
    }


    @Test
    void delete_TagDaoRemoveInvoked() throws DaoException {
        when(tagDao.getById(1L)).thenReturn(tag);
        tagService.delete(1L);
        verify(tagDao).remove(1L);
    }

    @Test
    void delete_ThrowsException() throws DaoException {
        doThrow(DaoException.class).when(tagDao).remove(1L);
        assertThrows(DaoException.class, () -> tagService.delete(1L));
    }

}