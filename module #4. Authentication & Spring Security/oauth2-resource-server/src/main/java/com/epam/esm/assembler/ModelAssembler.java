package com.epam.esm.assembler;

import org.springframework.data.domain.Page;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.RepresentationModel;

/**
 * An interface for assembling a DTO object with additional HATEOAS links using Spring HATEOAS
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

    /**
     * Converts a Page object of type T into a PagedModel.
     *
     * @param dtos    The page of entity objects to convert.
     * @param selfRel The HATEOAS link to add.
     * @return The PagedModel of entity objects with the links added.
     */
    PagedModel<T> toCollectionModel(Page<T> dtos, Link selfRel);
}
