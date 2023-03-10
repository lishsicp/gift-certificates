package com.epam.esm.service;

import com.epam.esm.dto.OrderDto;
import org.springframework.data.domain.Page;

/**
 * This interface provides business logic for the {@link OrderDto} entity.
 */
public interface OrderService extends CRDService<OrderDto> {

    /**
     * Retrieves a paginated list of {@link OrderDto orders} associated with the given {@link com.epam.esm.entity.User User} ID.
     *
     * @param id   the ID of the {@link com.epam.esm.entity.User User} to retrieve {@link OrderDto orders} for
     * @param page the page number of the result set to retrieve
     * @param size the number of items per page to retrieve
     * @return a {@link Page} object containing the requested list of {@link OrderDto} objects
     */
    Page<OrderDto> getOrdersByUserId(long id, int page, int size);
}
