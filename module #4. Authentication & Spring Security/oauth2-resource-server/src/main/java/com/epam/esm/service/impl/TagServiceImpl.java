package com.epam.esm.service.impl;

import com.epam.esm.dto.TagDto;
import com.epam.esm.dto.converter.TagConverter;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.ErrorCodes;
import com.epam.esm.exception.PersistentException;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;

    private final TagConverter tagConverter;

    @Override
    public Page<TagDto> getAll(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<Tag> tags = tagRepository.findAll(pageable);
        return tags.map(tagConverter::toDto);
    }

    @Override
    public TagDto getById(long id) throws PersistentException {
        Optional<Tag> optionalTag = tagRepository.findById(id);
        if (optionalTag.isEmpty()) {
            throw new PersistentException(ErrorCodes.RESOURCE_NOT_FOUND, id);
        }
        return tagConverter.toDto(optionalTag.get());
    }

    @Override
    public TagDto save(TagDto tagDto) throws PersistentException {
        Optional<Tag> optionalTag = tagRepository.findTagByName(tagDto.getName());
        if (optionalTag.isPresent()) {
            throw new PersistentException(ErrorCodes.DUPLICATED_TAG, tagDto.getName());
        }
        Tag savedTag = tagRepository.save(tagConverter.toEntity(tagDto));
        return tagConverter.toDto(savedTag);
    }

    @Override
    public void delete(long id) {
        Optional<Tag> tagOptional = tagRepository.findById(id);
        if (tagOptional.isEmpty()) {
            throw new PersistentException(ErrorCodes.RESOURCE_NOT_FOUND, id);
        }
        tagRepository.delete(tagOptional.get());
    }

    @Override
    public TagDto getMostWidelyUsedTagWithHighestCostOfAllOrders() {
        Optional<Tag> tagOptional = tagRepository.findMostWidelyUsedTagWithHighestCostOfAllOrders();
        if (tagOptional.isEmpty()) {
            throw new PersistentException(ErrorCodes.RESOURCE_NOT_FOUND, "id");
        }
        return tagConverter.toDto(tagOptional.get());
    }
}
