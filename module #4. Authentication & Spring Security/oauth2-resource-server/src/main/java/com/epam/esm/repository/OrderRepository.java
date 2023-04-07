package com.epam.esm.repository;

import com.epam.esm.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long>, PagingAndSortingRepository<Order, Long> {

    /**
     * Finds all {@link Order orders} belonging to the specified {@link com.epam.esm.entity.User User}.
     *
     * @param id       the id of the {@link com.epam.esm.entity.User User} to find {@link Order orders} for
     * @param pageable a {@link Pageable} object that specifies the requested page and page size for the result set
     * @return a {@link Page} object containing the requested list of {@link Order} objects
     */
    Page<Order> findOrdersByUserId(Long id, Pageable pageable);
}
