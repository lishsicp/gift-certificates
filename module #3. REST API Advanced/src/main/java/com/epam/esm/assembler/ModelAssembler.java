package com.epam.esm.assembler;

import org.springframework.hateoas.RepresentationModel;

/**
 * An interface for assembling a DTO object with additional HATEOAS links using Spring HATEOAS..
 *
 * @param <T> The type of the DTO class that extends {@link RepresentationModel} class.
 */
public interface ModelAssembler<T extends RepresentationModel<T>> {

    /**
     * Assembles a DTO object with additional HATEOAS links.
     *
     * @param t The DTO object to assemble.
     * @return The assembled DTO object with additional HATEOAS links.
     */
    T toModel(T t);
}
