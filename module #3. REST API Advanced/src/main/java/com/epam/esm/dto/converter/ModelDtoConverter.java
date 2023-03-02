package com.epam.esm.dto.converter;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * A base class for converting between model and DTO objects using ModelMapper..
 *
 * @param <T> The type of the entity model class.
 * @param <E> The type of the DTO class.
 */
@Component
public abstract class ModelDtoConverter<T, E> {

    /**
     * The ModelMapper instance used for mapping between model and DTO objects.
     */
    protected final ModelMapper modelMapper;

    @Autowired
    protected ModelDtoConverter(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    /**
     * Converts a DTO object to its corresponding entity model object.
     *
     * @param e The DTO object to convert.
     * @return The corresponding entity model object.
     */
    public abstract T toEntity(E e);

    /**
     * Converts an entity model object to its corresponding DTO object.
     *
     * @param t The entity model object to convert.
     * @return The corresponding DTO object.
     */
    public abstract E toDto(T t);
}