package com.epam.esm.service.impl;

import com.epam.esm.repository.TagRepository;
import com.epam.esm.entity.Tag;
import com.epam.esm.dto.TagDto;
import com.epam.esm.dto.converter.TagConverter;
import com.epam.esm.exception.PersistentException;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class TagServiceImplTest {

    @Mock
    private TagRepository repository;

    @Mock
    private TagConverter tagConverter;

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
    void getAll_shouldInvokeFindAll() {
        // given
        int PAGE = 1;
        int SIZE = 5;
        given(tagConverter.toDto(any())).willReturn(tagDto);
        given(repository.findAll(any(Pageable.class))).willReturn(new PageImpl<>(List.of(tag)));

        tagService.getAll(PAGE, SIZE);

        then(repository).should().findAll(any(Pageable.class));
    }

    @Test
    void getById_shouldReturnTag() {
        given(repository.findById(any())).willReturn(Optional.ofNullable(tag));
        given(tagConverter.toDto(any())).willReturn(tagDto);

        TagDto tagById = tagService.getById(1L);

        assertTrue(tagById.getId() > 0);
        assertEquals("tagTestName", tagById.getName());
    }

    @Test
    void getById_shouldThrowException_whenNonexistentId() {
        given(repository.findById(any())).willReturn(Optional.empty());

        assertThrows(PersistentException.class, () -> tagService.getById(1L));
    }

    @Test
    void delete_shouldInvokeTagDaoDelete() {
        given(repository.findById(1L)).willReturn(Optional.ofNullable(tag));
        willDoNothing().given(repository).delete(any());

        tagService.delete(1L);

        then(repository).should(times(1)).delete(any());
    }

    @Test
    void delete_shouldThrowException_whenNonexistentId() {
        given(repository.findById(any())).willReturn(Optional.empty());

        assertThrows(PersistentException.class, () -> tagService.delete(1L));
    }

    @Nested
    class WhenSaving {

        @Test
        void save_shouldInvokeTagDaoSave() {
            given(repository.findTagByName(anyString())).willReturn(Optional.empty());
            given(repository.save(any())).willReturn(tag);
            given(tagConverter.toEntity(any())).willReturn(tag);
            given(tagConverter.toDto(any())).willReturn(tagDto);

            TagDto savedTag = tagService.save(tagDto);

            assertEquals(tagDto, savedTag);
            then(repository).should(times(1)).save(tag);
        }

        @Test
        void save_shouldThrowException_whenNameAlreadyExist() {
            given(repository.findTagByName(anyString())).willReturn(Optional.of(tag));

            assertThrows(PersistentException.class, () -> tagService.save(tagDto));
        }
    }

    @Nested
    class WhenGettingMostWidelyUsedTagWithHighestCostOfAllOrders {

        @Test
        void getMostWidelyUsedTagWithHighestCostOfAllOrders_shouldReturnTag() {
            given(repository.findMostWidelyUsedTagWithHighestCostOfAllOrders()).willReturn(Optional.of(tag));
            given(tagConverter.toDto(any())).willReturn(tagDto);

            TagDto popularTag = tagService.getMostWidelyUsedTagWithHighestCostOfAllOrders();

            assertTrue(popularTag.getId() > 0);
            assertEquals("tagTestName", popularTag.getName());
        }

        @Test
        void getMostWidelyUsedTagWithHighestCostOfAllOrders_shouldThrowException_whenOptionalEmpty() {
            given(repository.findMostWidelyUsedTagWithHighestCostOfAllOrders()).willReturn(Optional.empty());

            assertThrows(PersistentException.class, () -> tagService.getMostWidelyUsedTagWithHighestCostOfAllOrders());
        }

    }
}