package com.epam.esm.dto.converter;

import com.epam.esm.dto.TagDto;
import com.epam.esm.entity.Tag;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class TagConverter extends ModelDtoConverter<TagDto, Tag> {

    public TagConverter(ModelMapper modelMapper) {
        super(modelMapper);
    }

    @Override
    public Tag toEntity(TagDto tagDto) {
        return modelMapper.map(tagDto, Tag.class);
    }

    @Override
    public TagDto toDto(Tag tag) {
        return modelMapper.map(tag, TagDto.class);
    }
}
