package com.epam.esm.service.impl;

import com.epam.esm.repository.GiftCertificateRepository;
import com.epam.esm.repository.OrderRepository;
import com.epam.esm.repository.UserRepository;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Order;
import com.epam.esm.entity.User;
import com.epam.esm.service.OrderService;
import com.epam.esm.dto.OrderDto;
import com.epam.esm.dto.converter.OrderConverter;
import com.epam.esm.exception.ErrorCodes;
import com.epam.esm.exception.PersistentException;
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

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final GiftCertificateRepository giftCertificateRepository;
    private final OrderConverter orderConverter;

    @Autowired
    public OrderServiceImpl(OrderRepository orderDao,
                            UserRepository userDao,
                            GiftCertificateRepository giftCertificateDao,
                            OrderConverter orderConverter) {
        this.orderRepository = orderDao;
        this.userRepository = userDao;
        this.giftCertificateRepository = giftCertificateDao;
        this.orderConverter = orderConverter;
    }

    @Override
    public OrderDto save(OrderDto orderDto) {
        Order order = orderConverter.toEntity(orderDto);
        long certificateId = order.getGiftCertificate().getId();
        Optional<GiftCertificate> certificateOptional = giftCertificateRepository.findById(certificateId);
        if (certificateOptional.isEmpty()) {
            throw new PersistentException(ErrorCodes.RESOURCE_NOT_FOUND, certificateId);
        }

        long userId = order.getUser().getId();
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            throw new PersistentException(ErrorCodes.RESOURCE_NOT_FOUND, userId);
        }

        order.setUser(userOptional.get());
        order.setGiftCertificate(certificateOptional.get());
        order.setCost(certificateOptional.get().getPrice());
        order.setPurchaseDate(LocalDateTime.now(zoneId));

        return orderConverter.toDto(orderRepository.save(order));
    }

    @Override
    public Page<OrderDto> getOrdersByUserId(long id, int page, int size) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isEmpty()) {
            throw new PersistentException(ErrorCodes.RESOURCE_NOT_FOUND, id);
        }
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<Order> ordersByUserId = orderRepository.findOrdersByUserId(id, pageable);
        return ordersByUserId.map(orderConverter::toDto);
    }

    @Override
    public Page<OrderDto> getAll(int page, int size) {
        throw new UnsupportedOperationException();
    }

    @Override
    public OrderDto getById(long id)  {
        Optional<Order> orderOptional = orderRepository.findById(id);
        if (orderOptional.isEmpty()) {
            throw new PersistentException(ErrorCodes.RESOURCE_NOT_FOUND, id);
        }
        return orderConverter.toDto(orderOptional.get());
    }

    @Override
    public void delete(long id) {
        Optional<Order> orderOptional = orderRepository.findById(id);
        if (orderOptional.isEmpty()) {
            throw new PersistentException(ErrorCodes.RESOURCE_NOT_FOUND, id);
        }
        orderRepository.delete(orderOptional.get());
    }
}
