package com.epam.esm.service.impl;


import com.epam.esm.dto.MakeOrderDto;
import com.epam.esm.dto.OrderDto;
import com.epam.esm.exception.PersistentException;
import com.epam.esm.extension.TestContainerExtension;
import com.epam.esm.repository.GiftCertificateRepository;
import com.epam.esm.repository.OrderRepository;
import com.epam.esm.repository.UserRepository;
import com.epam.esm.service.OrderService;
import com.epam.esm.util.ModelFactory;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(TestContainerExtension.class)
@SpringBootTest
@ActiveProfiles("test")
@Transactional
class OrderServiceImplIT {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GiftCertificateRepository certificateRepository;

    @Autowired
    private OrderService service;

    @Test
    void findById_shouldReturnOrder() {
        var order = orderRepository.save(ModelFactory.createNewOrder());
        var orderDto = ModelFactory.toOrderDto(order);

        OrderDto actual = service.getById(orderDto.getId());

        assertEquals(orderDto, actual);
    }

    @Test
    void findById_shouldThrowException_whenNonExistentId() {
        assertThrows(PersistentException.class, () -> service.getById(0));
    }

    @Nested
    class WhenSaving {

        @Test
        void save_shouldThrowException_whenCertificateIsEmpty() {
            var order = new MakeOrderDto();
            var user = userRepository.save(ModelFactory.createNewUser());
            order.setUserId(user.getId());
            assertThrows(PersistentException.class, () -> service.save(order));
        }

        @Test
        void save_shouldThrowException_whenUserIsEmpty() {
            var order = new MakeOrderDto();
            var certificate = certificateRepository.save(ModelFactory.createNewGiftCertificate());
            order.setGiftCertificateId(certificate.getId());
            assertThrows(PersistentException.class, () -> service.save(order));
        }

        @Test
        void save_shouldSave() {
            var order = new MakeOrderDto();
            var certificate = certificateRepository.save(ModelFactory.createNewGiftCertificate());
            var user = userRepository.save(ModelFactory.createNewUser());
            order.setGiftCertificateId(certificate.getId());
            order.setUserId(user.getId());

            OrderDto actual = service.save(order);

            assertTrue(actual.getId() > 0);
            assertNotNull(actual.getCost());
            assertNotNull(actual.getPurchaseDate());
            assertNotNull(actual.getUser());
            assertNotNull(actual.getGiftCertificate());
            assertEquals(order.getUserId(), actual.getUser().getId());
            assertEquals(order.getGiftCertificateId(), actual.getGiftCertificate().getId());

        }
    }

    @Nested
    class WhenGettingOrdersByUserId {

        @Test
        void getOrdersByUserId_shouldReturnOrderList() {
            // given
            var order = ModelFactory.createNewOrder();

            var user = userRepository.save(ModelFactory.createNewUser());
            var certificate = certificateRepository.save(ModelFactory.createNewGiftCertificate());

            order.setUser(user);
            order.setGiftCertificate(certificate);

            var savedOrder = orderRepository.save(order);
            var orderDto = ModelFactory.toOrderDto(savedOrder);

            // when
            Page<OrderDto> actual = service.getOrdersByUserId(user.getId(), 1, 1);

            // then
            assertEquals(Collections.singletonList(orderDto), actual.getContent());
        }

        @Test
        void getOrdersByUserId_shouldThrowException() {
            assertThrows(PersistentException.class, () -> service.getOrdersByUserId(0, 0, 0));
        }
    }
}