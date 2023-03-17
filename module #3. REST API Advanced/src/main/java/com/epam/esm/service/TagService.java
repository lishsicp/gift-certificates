package com.epam.esm.service;

import com.epam.esm.dto.TagDto;

/**
 * This interface provides business logic for the {@link TagDto} entity.
 */
public interface TagService extends CRDService<TagDto> {

    /**
     * Retrieves the {@link TagDto} with the highest total cost of all orders it has been associated with, and is also
     * the most widely used Tag.
     *
     * @return a {@link TagDto} object
     */
    TagDto getMostWidelyUsedTagWithHighestCostOfAllOrders();
}
