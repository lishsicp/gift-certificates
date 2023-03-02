package com.epam.esm.repository;

import com.epam.esm.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 * This interface extends Spring's PagingAndSortingRepository interface for the Order entity, adding a custom method
 * for finding all orders belonging to a specified user.
 */
@Repository
public interface OrderRepository extends PagingAndSortingRepository<Order, Long> {

    /**
     * Finds all orders belonging to the specified user.
     *
     * @param id       the id of the user to find orders for
     * @param pageable a Pageable object that specifies the requested page and page size for the result set
     * @return a Page object containing the requested list of Order objects
     */
    Page<Order> findOrdersByUserId(Long id, Pageable pageable);
}
