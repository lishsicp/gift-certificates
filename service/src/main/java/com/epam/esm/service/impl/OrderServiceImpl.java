package com.epam.esm.service.impl;

import com.epam.esm.repository.GiftCertificateRepository;
import com.epam.esm.repository.OrderRepository;
import com.epam.esm.repository.UserRepository;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Order;
import com.epam.esm.entity.User;
import com.epam.esm.service.OrderService;
import com.epam.esm.service.exception.ExceptionErrorCode;
import com.epam.esm.service.exception.PersistentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

@Service
public class OrderServiceImpl implements OrderService {

    private final ZoneId zoneId = ZoneId.systemDefault();

    private final OrderRepository orderDao;
    private final UserRepository userDao;
    private final GiftCertificateRepository giftCertificateDao;

    @Autowired
    public OrderServiceImpl(OrderRepository orderDao, UserRepository userDao, GiftCertificateRepository giftCertificateDao) {
        this.orderDao = orderDao;
        this.userDao = userDao;
        this.giftCertificateDao = giftCertificateDao;
    }

    @Override
    public Order save(Order order) throws PersistentException {
        Long certificateId = order.getGiftCertificate().getId();
        Optional<GiftCertificate> certificateOptional = giftCertificateDao.findById(certificateId);
        if (certificateOptional.isEmpty())
            throw new PersistentException(ExceptionErrorCode.RESOURCE_NOT_FOUND, certificateId);

        Long userId = order.getUser().getId();
        Optional<User> userOptional = userDao.findById(userId);
        if (userOptional.isEmpty())
            throw new PersistentException(ExceptionErrorCode.RESOURCE_NOT_FOUND, userId);

        order.setUser(userOptional.get());
        order.setGiftCertificate(certificateOptional.get());
        order.setCost(certificateOptional.get().getPrice());
        order.setPurchaseDate(LocalDateTime.now(zoneId));

        return orderDao.save(order);
    }

    @Override
    public List<Order> getOrdersByUserId(Long id, int page, int size) throws PersistentException {
        Optional<User> userOptional = userDao.findById(id);
        if (userOptional.isEmpty())
            throw new PersistentException(ExceptionErrorCode.RESOURCE_NOT_FOUND, id);
        Pageable pageable = PageRequest.of(page - 1, size);
        return orderDao.findOrdersByUserId(id, pageable);
    }

    @Override
    public Page<Order> getAll(int page, int size) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Order getById(Long id) throws PersistentException {
        return orderDao.findById(id).orElseThrow(() -> new PersistentException(ExceptionErrorCode.RESOURCE_NOT_FOUND, id));
    }

    @Override
    public void delete(Long id) throws PersistentException {
        throw new UnsupportedOperationException();
    }
}
