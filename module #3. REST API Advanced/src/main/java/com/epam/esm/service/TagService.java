package com.epam.esm.service;

import com.epam.esm.dto.TagDto;

/**
 * This interface provides data access functionality for the Tag entity. It extends the CRDService interface and adds a
 * <p>
 * method for retrieving the most widely used Tag with the highest cost of all orders.
 */
public interface TagService extends CRDService<TagDto> {

    /**
     * Retrieves the Tag with the highest total cost of all orders it has been associated with, and is also the most
     * widely used Tag.
     *
     * @return a TagDto object representing the Tag with the highest total cost and widest usage
     */
    TagDto getMostWidelyUsedTagWithHighestCostOfAllOrders();
}
