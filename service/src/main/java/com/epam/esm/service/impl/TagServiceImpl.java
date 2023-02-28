package com.epam.esm.service.impl;

import com.epam.esm.repository.TagRepository;
import com.epam.esm.entity.Tag;
import com.epam.esm.service.TagService;
import com.epam.esm.service.dto.TagDto;
import com.epam.esm.service.dto.converter.TagConverter;
import com.epam.esm.service.exception.ErrorCodes;
import com.epam.esm.service.exception.PersistentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;

    private final TagConverter tagConverter;

    @Autowired
    public TagServiceImpl(TagRepository tagRepository, TagConverter tagConverter) {
        this.tagRepository = tagRepository;
        this.tagConverter = tagConverter;
    }

    @Override
    public Page<TagDto> getAll(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<Tag> tags = tagRepository.findAll(pageable);
        return tags.map(tagConverter::toDto);
    }

    @Override
    public TagDto getById(long id) throws PersistentException {
        Tag tag = tagRepository.findById(id).orElseThrow(() -> new PersistentException(ErrorCodes.RESOURCE_NOT_FOUND, id));
        return tagConverter.toDto(tag);
    }

    @Override
    public TagDto save(TagDto tagDto) throws PersistentException {
        Optional<Tag> optionalTag = tagRepository.findTagByName(tagDto.getName());
        if (optionalTag.isPresent())
            throw new PersistentException(ErrorCodes.DUPLICATED_TAG, tagDto.getName());
        Tag save = tagRepository.save(tagConverter.toEntity(tagDto));
        return tagConverter.toDto(save);
    }

    @Override
    public void delete(long id) throws PersistentException {
        Optional<Tag> tagOptional = tagRepository.findById(id);
        if (tagOptional.isEmpty())
            throw new PersistentException(ErrorCodes.RESOURCE_NOT_FOUND, id);
        tagRepository.delete(tagOptional.get());
    }

    @Override
    public TagDto getMostWidelyUsedTagWithHighestCostOfAllOrders() {
        Tag tag = tagRepository
                .findMostWidelyUsedTagWithHighestCostOfAllOrders()
                .orElseThrow(
                        () -> new PersistentException(ErrorCodes.RESOURCE_NOT_FOUND, "id")
                );
        return tagConverter.toDto(tag);
    }
}
