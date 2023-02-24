package com.epam.esm.service;

import com.epam.esm.service.dto.OrderDto;
import org.springframework.data.domain.Page;


/**
 * The OrderService interface extends the CRDService interface for managing OrderDto objects.
 * <p>
 * It provides methods for creating, reading, and deleting OrderDto objects.
 * <p>
 * It also defines a method for retrieving a paginated list of orders by user ID.
 */
public interface OrderService extends CRDService<OrderDto> {

    /**
     * Retrieves a paginated list of orders associated with the given user ID.
     *
     * @param id   the ID of the user to retrieve orders for
     * @param page the page number of the result set to retrieve
     * @param size the number of items per page to retrieve
     * @return a Page object containing the requested list of OrderDto objects
     */
    Page<OrderDto> getOrdersByUserId(long id, int page, int size);
}
