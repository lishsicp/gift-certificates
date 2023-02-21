package com.epam.esm.service.impl;

import com.epam.esm.repository.TagRepository;
import com.epam.esm.entity.Tag;
import com.epam.esm.service.dto.TagDto;
import com.epam.esm.service.dto.converter.TagConverter;
import com.epam.esm.service.exception.PersistentException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TagServiceImplTest {

    @Mock
    private static TagRepository repository;

    @Mock
    private static TagConverter tagConverter;

    @InjectMocks
    private TagServiceImpl tagService;

    private Tag tag;
    private TagDto tagDto;

    @BeforeEach
    void setup() {
        tag = Tag.builder().id(1L).name("tagTestName").build();
        tagDto = TagDto.builder().id(1L).name("tagTestName").build();
    }

    @Test
    void testFindAll_ShouldInvokeTagDaoFindAll() {
        int PAGE = 1;
        int SIZE = 5;
        when(tagConverter.toDto(any())).thenReturn(tagDto);
        when(repository.findAll(any(Pageable.class))).thenReturn(new PageImpl<>(List.of(tag)));
        tagService.getAll(PAGE, SIZE);
        verify(repository).findAll(any(Pageable.class));
    }

    @Test
    void testFindById_ShouldReturnTag() {
        when(repository.findById(any())).thenReturn(Optional.ofNullable(tag));
        when(tagConverter.toDto(any())).thenReturn(tagDto);
        TagDto tagById = tagService.getById(1L);
        assertTrue(tagById.getId() > 0);
        assertEquals("tagTestName", tagById.getName());
    }

    @Test
    void testFindById_ShouldThrowException() {
        assertThrows(PersistentException.class, () -> tagService.getById(1L));
    }

    @Test
    void testDelete_ShouldInvokeTagDaoDelete() {
        when(repository.findById(1L)).thenReturn(Optional.ofNullable(tag));
        doNothing().when(repository).delete(any());
        tagService.delete(1L);
        verify(repository, times(1)).delete(any());
    }

    @Test
    void testDelete_ShouldThrowException() {
        assertThrows(PersistentException.class, () -> tagService.delete(1L));
    }

    @Nested
    class WhenSaving {

        @Test
        void testSave_ShouldInvokeTagDaoSave() {
            when(repository.findTagByName(anyString())).thenReturn(Optional.empty());
            when(repository.save(any())).thenReturn(tag);
            when(tagConverter.toEntity(any())).thenReturn(tag);
            when(tagConverter.toDto(any())).thenReturn(tagDto);
            TagDto savedTag = tagService.save(tagDto);
            assertEquals(tagDto, savedTag);
            verify(repository, times(1)).save(tag);
        }

        @Test
        void testSave_ShouldThrowException() {
            when(repository.findTagByName(anyString())).thenReturn(Optional.of(tag));
            assertThrows(PersistentException.class, () -> tagService.save(tagDto));
        }
    }

    @Nested
    class WhenGettingMostWidelyUsedTagWithHighestCostOfAllOrders {

        @Test
        void testGetMostWidelyUsedTagWithHighestCostOfAllOrders_ShouldReturnTag() {
            when(repository.findMostWidelyUsedTagWithHighestCostOfAllOrders()).thenReturn(Optional.of(tag));
            when(tagConverter.toDto(any())).thenReturn(tagDto);
            TagDto popularTag = tagService.getMostWidelyUsedTagWithHighestCostOfAllOrders();
            assertTrue(popularTag.getId() > 0);
            assertEquals("tagTestName", popularTag.getName());
        }

        @Test
        void testGetMostWidelyUsedTagWithHighestCostOfAllOrders_ShouldThrowException() {
            when(repository.findMostWidelyUsedTagWithHighestCostOfAllOrders()).thenReturn(Optional.empty());
            assertThrows(PersistentException.class, () -> tagService.getMostWidelyUsedTagWithHighestCostOfAllOrders());
        }

    }
}