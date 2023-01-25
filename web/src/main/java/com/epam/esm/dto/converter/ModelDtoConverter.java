package com.epam.esm.dto.converter;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public abstract class ModelDtoConverter<T, E> {

    protected final ModelMapper modelMapper;

    @Autowired
    protected ModelDtoConverter(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public abstract T toEntity(E e);

    public abstract E toDto(T t);
}
