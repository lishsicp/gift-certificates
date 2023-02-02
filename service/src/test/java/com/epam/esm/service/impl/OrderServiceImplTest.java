package com.epam.esm.service.impl;


import com.epam.esm.dao.GiftCertificateDao;
import com.epam.esm.dao.OrderDao;
import com.epam.esm.dao.UserDao;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Order;
import com.epam.esm.entity.User;
import com.epam.esm.service.exception.PersistentException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

    @Mock
    private OrderDao orderDao;
    @Mock
    private UserDao userDao;
    @Mock
    private GiftCertificateDao giftCertificateDao;

    @InjectMocks
    private OrderServiceImpl service;

    private Order order;

    @BeforeEach
    void setUp() {
        User user = new User();
        user.setId(1L);
        user.setName("User Name");

        GiftCertificate certificate = new GiftCertificate();
        certificate.setId(1L);

        order = new Order();
        order.setId(1L);
        order.setUser(user);
        order.setCost(BigDecimal.TEN);
        order.setPurchaseDate(LocalDateTime.now());
        order.setGiftCertificate(certificate);
        order.setUser(user);
    }

    @Test
    void testFindById_ShouldReturnOrder() {
        when(orderDao.findById(anyLong())).thenReturn(Optional.ofNullable(order));
        Order actual = service.getById(anyLong());
        assertEquals(order, actual);
    }

    @Test
    void testFindById_ShouldThrowException() {
        Long userId = order.getId();
        assertThrows(PersistentException.class, () -> service.getById(userId));
    }

    @Nested
    class WhenSaving {

        @Test
        void testSave_ShouldThrowExceptionIfCertificateIsEmpty() {
            assertThrows(PersistentException.class, () -> service.save(order));
        }

        @Test
        void testSave_ShouldThrowExceptionIfUserIsEmpty() {
            assertThrows(PersistentException.class, () -> service.save(order));
        }

        @Test
        void testSave_ShouldSave() {
            when(giftCertificateDao.findById(anyLong())).thenReturn(Optional.ofNullable(order.getGiftCertificate()));
            when(userDao.findById(anyLong())).thenReturn(Optional.ofNullable(order.getUser()));
            when(orderDao.save(order)).thenReturn(order);
            Order actual = service.save(order);
            assertEquals(order, actual);
        }
    }

    @Nested
    class WhenGettingOrdersByUserId {
        private final int PAGE = 61;
        private final int SIZE = 65;

        @Test
        void testGetOrdersByUserId_ShouldReturnOrderList() {
            when(userDao.findById(anyLong())).thenReturn(Optional.ofNullable(order.getUser()));
            when(orderDao.findOrdersByUserId(anyLong(), any())).thenReturn(Collections.singletonList(order));
            List<Order> actual = service.getOrdersByUserId(anyLong(), PAGE, SIZE);
            assertEquals(Collections.singletonList(order), actual);
        }

        @Test
        void testGetOrdersByUserId_ShouldThrowException() {
            Long id = order.getId();
            assertThrows(PersistentException.class, () -> service.getOrdersByUserId(id, PAGE, SIZE));
        }

    }
}