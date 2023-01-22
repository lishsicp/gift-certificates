package com.epam.esm.dto.converter;

import org.hibernate.collection.spi.PersistentCollection;
import org.modelmapper.Condition;
import org.modelmapper.ModelMapper;
import org.modelmapper.spi.MappingContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public abstract class ModelDtoConverter<T, E> {

    protected final ModelMapper modelMapper;

    @Autowired
    protected ModelDtoConverter(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }
    public abstract E toEntity(T t);
    public abstract T toDto(E e);
}
