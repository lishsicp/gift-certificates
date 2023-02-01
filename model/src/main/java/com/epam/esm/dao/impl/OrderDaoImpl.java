package com.epam.esm.dao.impl;

import com.epam.esm.dao.GenericDao;
import com.epam.esm.dao.OrderDao;
import com.epam.esm.entity.Order;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class OrderDaoImpl extends GenericDao<Order> implements OrderDao {

    protected OrderDaoImpl() {
        super(Order.class);
    }

    @Override
    public List<Order> findOrdersByUserId(Long id, Pageable pageable) {
        return entityManager
                .createQuery("select o from Order o where o.user.id = :id", Order.class)
                .setParameter("id", id)
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize())
                .getResultList();
    }

    @Override
    public Order update(Order order) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<Order> findAll(Pageable pageable) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void delete(Long id) {
        throw new UnsupportedOperationException();
    }
}
