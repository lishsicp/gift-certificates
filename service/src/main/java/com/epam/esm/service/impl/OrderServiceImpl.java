package com.epam.esm.service.impl;

import com.epam.esm.repository.GiftCertificateRepository;
import com.epam.esm.repository.OrderRepository;
import com.epam.esm.repository.UserRepository;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Order;
import com.epam.esm.entity.User;
import com.epam.esm.service.OrderService;
import com.epam.esm.service.dto.OrderDto;
import com.epam.esm.service.dto.converter.OrderConverter;
import com.epam.esm.service.exception.ExceptionErrorCode;
import com.epam.esm.service.exception.PersistentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;

@Service
public class OrderServiceImpl implements OrderService {

    private final ZoneId zoneId = ZoneId.systemDefault();

    private final OrderRepository orderDao;
    private final UserRepository userDao;
    private final GiftCertificateRepository giftCertificateDao;
    private final OrderConverter orderConverter;

    @Autowired
    public OrderServiceImpl(OrderRepository orderDao,
                            UserRepository userDao,
                            GiftCertificateRepository giftCertificateDao,
                            OrderConverter orderConverter) {
        this.orderDao = orderDao;
        this.userDao = userDao;
        this.giftCertificateDao = giftCertificateDao;
        this.orderConverter = orderConverter;
    }

    @Override
    public OrderDto save(OrderDto orderDto) throws PersistentException {
        Order order = orderConverter.toEntity(orderDto);
        long certificateId = order.getGiftCertificate().getId();
        Optional<GiftCertificate> certificateOptional = giftCertificateDao.findById(certificateId);
        if (certificateOptional.isEmpty())
            throw new PersistentException(ExceptionErrorCode.RESOURCE_NOT_FOUND, certificateId);

        long userId = order.getUser().getId();
        Optional<User> userOptional = userDao.findById(userId);
        if (userOptional.isEmpty())
            throw new PersistentException(ExceptionErrorCode.RESOURCE_NOT_FOUND, userId);
        order.setUser(userOptional.get());
        order.setGiftCertificate(certificateOptional.get());
        order.setCost(certificateOptional.get().getPrice());
        order.setPurchaseDate(LocalDateTime.now(zoneId));

        return orderConverter.toDto(orderDao.save(order));
    }

    @Override
    public Page<OrderDto> getOrdersByUserId(long id, int page, int size) throws PersistentException {
        Optional<User> userOptional = userDao.findById(id);
        if (userOptional.isEmpty())
            throw new PersistentException(ExceptionErrorCode.RESOURCE_NOT_FOUND, id);
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<Order> ordersByUserId = orderDao.findOrdersByUserId(id, pageable);
        return ordersByUserId.map(orderConverter::toDto);
    }

    @Override
    public Page<OrderDto> getAll(int page, int size) {
        throw new UnsupportedOperationException();
    }

    @Override
    public OrderDto getById(long id) throws PersistentException {
        Order order = orderDao.findById(id).orElseThrow(() -> new PersistentException(ExceptionErrorCode.RESOURCE_NOT_FOUND, id));
        return orderConverter.toDto(order);
    }

    @Override
    public void delete(long id) throws PersistentException {
        throw new UnsupportedOperationException();
    }
}
