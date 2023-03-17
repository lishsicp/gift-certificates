package com.epam.esm.service.impl;

import com.epam.esm.dto.TagDto;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.PersistentException;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.extension.PostgresExtension;
import com.epam.esm.service.TagService;
import com.epam.esm.util.ModelFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(PostgresExtension.class)
@SpringBootTest
@Transactional
@ActiveProfiles("test")
class TagServiceImplIT {

    @Autowired
    private TagService tagService;

    @Autowired
    private TagRepository tagRepository;

    @Test
    void getById_shouldReturnTag() {
        var newTag = tagRepository.save(ModelFactory.createNewTag());
        var expected = ModelFactory.toTagDto(newTag);

        TagDto tagDto = tagService.getById(newTag.getId());

        assertEquals(expected, tagDto);
    }

    @Test
    void getById_shouldThrowException() {
        assertThrows(PersistentException.class, () -> tagService.getById(0));
    }

    @Test
    void getAll_shouldReturnTwoTagsWithPagedMetadata() {
        List<Tag> savedTags = tagRepository.saveAll(List.of(ModelFactory.createNewTag(), ModelFactory.createNewTag()));
        List<TagDto> savedTagsToDto = savedTags.stream().map(ModelFactory::toTagDto).collect(Collectors.toList());
        Page<TagDto> savedTagsPaged = new PageImpl<>(savedTagsToDto, PageRequest.of(0, 5), savedTagsToDto.size());

        Page<TagDto> tagDtos = tagService.getAll(1, 5);

        assertEquals(savedTagsPaged, tagDtos);
    }

    @Test
    void save_shouldSave() {
        Tag newTag = ModelFactory.createNewTag();
        TagDto newTagDto = ModelFactory.toTagDto(newTag);

        TagDto savedTag = tagService.save(newTagDto);

        assertEquals(newTag.getName(), savedTag.getName());
    }

    @Test
    void save_shouldThrowException_whenTagExists() {
        Tag newTag = tagRepository.save(ModelFactory.createNewTag());
        TagDto newTagDto = ModelFactory.toTagDto(newTag);
        assertThrows(PersistentException.class, () -> tagService.save(newTagDto));
    }

    @Test
    void delete_shouldReturnEmptyOptional_whenTagWasDeleted() {
        var newTag = tagRepository.save(ModelFactory.createNewTag());

        tagService.delete(newTag.getId());

        assertEquals(Optional.empty(), tagRepository.findById(newTag.getId()));
    }

    @Test
    void delete_shouldThrowException_whenTagDoNotExist() {
        assertThrows(PersistentException.class, () -> tagService.delete(0));
    }

}