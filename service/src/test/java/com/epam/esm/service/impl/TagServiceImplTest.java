package com.epam.esm.service.impl;

import com.epam.esm.dao.TagDao;
import com.epam.esm.entity.Tag;
import com.epam.esm.service.exception.PersistentException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TagServiceImplTest {

    @Mock
    private static TagDao tagDao;

    @InjectMocks
    private TagServiceImpl tagService;

    private Tag tag;

    @BeforeEach
    void setup() {
        tag = Tag.builder().id(1L).name("tagTestName").build();
    }

    @Test
    void testFindAll_ShouldInvokeTagDaoFindAll() {
        int PAGE = 1;
        int SIZE = 5;
        tagService.getAll(PAGE, SIZE);
        verify(tagDao).findAll(any());
    }

    @Test
    void testFindById_ShouldReturnTag() {
        when(tagDao.findById(any())).thenReturn(Optional.ofNullable(tag));
        Tag tagById = tagService.getById(1L);
        assertTrue(tagById.getId() > 0);
        assertEquals("tagTestName", tagById.getName());
    }

    @Test
    void testFindById_ShouldThrowException() {
        assertThrows(PersistentException.class, () -> tagService.getById(1L));
    }

    @Test
    void testDelete_ShouldInvokeTagDaoDelete() {
        when(tagDao.findById(1L)).thenReturn(Optional.ofNullable(tag));
        doNothing().when(tagDao).delete(any());
        tagService.delete(1L);
        verify(tagDao, times(1)).delete(any());
    }

    @Test
    void testDelete_ShouldThrowException() {
        assertThrows(PersistentException.class, () -> tagService.delete(1L));
    }

    @Nested
    class WhenSaving {

        @Test
        void testSave_ShouldInvokeTagDaoSave() {
            when(tagDao.findByName(anyString())).thenReturn(Optional.empty());
            when(tagDao.save(any())).thenReturn(tag);
            Tag savedTag = tagService.save(tag);
            assertEquals(tag, savedTag);
            verify(tagDao, times(1)).save(tag);
        }

        @Test
        void testSave_ShouldThrowException() {
            when(tagDao.findByName(anyString())).thenReturn(Optional.of(tag));
            assertThrows(PersistentException.class, () -> tagService.save(tag));
        }
    }

    @Nested
    class WhenGettingMostWidelyUsedTagWithHighestCostOfAllOrders {

        @Test
        void testGetMostWidelyUsedTagWithHighestCostOfAllOrders_ShouldReturnTag() {
            when(tagDao.findMostWidelyUsedTagWithHighestCostOfAllOrders()).thenReturn(Optional.of(tag));

            Tag popularTag = tagService.getMostWidelyUsedTagWithHighestCostOfAllOrders();

            assertTrue(popularTag.getId() > 0);
            assertEquals("tagTestName", popularTag.getName());
        }

        @Test
        void testGetMostWidelyUsedTagWithHighestCostOfAllOrders_ShouldThrowException() {
            when(tagDao.findMostWidelyUsedTagWithHighestCostOfAllOrders()).thenReturn(Optional.empty());
            assertThrows(PersistentException.class, () -> tagService.getMostWidelyUsedTagWithHighestCostOfAllOrders());
        }

    }
}