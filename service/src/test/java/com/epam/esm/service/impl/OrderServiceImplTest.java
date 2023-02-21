package com.epam.esm.service.impl;


import com.epam.esm.repository.GiftCertificateRepository;
import com.epam.esm.repository.OrderRepository;
import com.epam.esm.repository.UserRepository;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Order;
import com.epam.esm.entity.User;
import com.epam.esm.service.dto.GiftCertificateDto;
import com.epam.esm.service.dto.OrderDto;
import com.epam.esm.service.dto.UserDto;
import com.epam.esm.service.dto.converter.OrderConverter;
import com.epam.esm.service.exception.PersistentException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

    @Mock
    private OrderRepository orderRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private GiftCertificateRepository certificateRepository;
    @Mock
    private OrderConverter orderConverter;

    @InjectMocks
    private OrderServiceImpl service;

    private Order order;
    private OrderDto orderDto;

    @BeforeEach
    void setUp() {
        User user = User.builder().id(1).name("User Name").build();
        UserDto userDto = UserDto.builder().id(1L).name("User Name").build();
        GiftCertificate certificate = GiftCertificate.builder().id(1).build();
        GiftCertificateDto certificateDto = GiftCertificateDto.builder().id(1L).build();
        order = Order.builder().id(1).user(user)
                .giftCertificate(certificate)
                .purchaseDate(LocalDateTime.now())
                .cost(BigDecimal.TEN).build();

        orderDto = OrderDto.builder().id(1L).user(userDto)
                .giftCertificate(certificateDto)
                .purchaseDate(LocalDateTime.now())
                .cost(BigDecimal.TEN).build();
    }

    @Test
    void testFindById_ShouldReturnOrder() {
        when(orderRepository.findById(anyLong())).thenReturn(Optional.ofNullable(order));
        when(orderConverter.toDto(any())).thenReturn(orderDto);
        OrderDto actual = service.getById(anyLong());
        assertEquals(orderDto, actual);
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
            assertThrows(PersistentException.class, () -> service.save(orderDto));
        }

        @Test
        void testSave_ShouldThrowExceptionIfUserIsEmpty() {
            assertThrows(PersistentException.class, () -> service.save(orderDto));
        }

        @Test
        void testSave_ShouldSave() {
            when(certificateRepository.findById(anyLong())).thenReturn(Optional.ofNullable(order.getGiftCertificate()));
            when(userRepository.findById(anyLong())).thenReturn(Optional.ofNullable(order.getUser()));
            when(orderConverter.toEntity(any())).thenReturn(order);
            when(orderRepository.save(order)).thenReturn(order);
            when(orderConverter.toDto(any())).thenReturn(orderDto);
            OrderDto actual = service.save(orderDto);
            assertEquals(orderDto, actual);
        }
    }

    @Nested
    class WhenGettingOrdersByUserId {
        private final int PAGE = 61;
        private final int SIZE = 65;

        @Test
        void testGetOrdersByUserId_ShouldReturnOrderList() {
            when(userRepository.findById(anyLong())).thenReturn(Optional.ofNullable(order.getUser()));
            when(orderRepository.findOrdersByUserId(anyLong(), any())).thenReturn(new PageImpl<>(Collections.singletonList(order)));
            when(orderConverter.toDto(any())).thenReturn(orderDto);
            Page<OrderDto> actual = service.getOrdersByUserId(anyLong(), PAGE, SIZE);
            assertEquals(Collections.singletonList(orderDto), actual.getContent());
        }

        @Test
        void testGetOrdersByUserId_ShouldThrowException() {
            Long id = order.getId();
            assertThrows(PersistentException.class, () -> service.getOrdersByUserId(id, PAGE, SIZE));
        }

    }
}