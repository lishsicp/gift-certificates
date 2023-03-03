package com.epam.esm.service.impl;


import com.epam.esm.dto.OrderDto;
import com.epam.esm.dto.converter.OrderConverter;
import com.epam.esm.exception.PersistentException;
import com.epam.esm.repository.GiftCertificateRepository;
import com.epam.esm.repository.OrderRepository;
import com.epam.esm.repository.UserRepository;
import com.epam.esm.util.ModelFactory;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyLong;

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

    @Test
    void findById_shouldReturnOrder() {
        var order = ModelFactory.createOrder();
        var orderDto = ModelFactory.toOrderDto(order);
        given(orderRepository.findById(anyLong())).willReturn(Optional.ofNullable(order));
        given(orderConverter.toDto(any())).willReturn(orderDto);

        OrderDto actual = service.getById(anyLong());

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
            var order = ModelFactory.createOrder();
            var orderDto = ModelFactory.toOrderDto(order);
            given(orderConverter.toEntity(any())).willReturn(order);
            assertThrows(PersistentException.class, () -> service.save(orderDto));
        }

        @Test
        void save_shouldThrowException_whenUserIsEmpty() {
            var order = ModelFactory.createOrder();
            var orderDto = ModelFactory.toOrderDto(order);
            given(orderConverter.toEntity(any())).willReturn(order);
            assertThrows(PersistentException.class, () -> service.save(orderDto));
        }

        @Test
        void save_shouldSave() {
            var order = ModelFactory.createOrder();
            var orderDto = ModelFactory.toOrderDto(order);
            given(certificateRepository.findById(anyLong())).willReturn(Optional.ofNullable(order.getGiftCertificate()));
            given(userRepository.findById(anyLong())).willReturn(Optional.ofNullable(order.getUser()));
            given(orderConverter.toEntity(any())).willReturn(order);
            given(orderRepository.save(order)).willReturn(order);
            given(orderConverter.toDto(any())).willReturn(orderDto);

            OrderDto actual = service.save(orderDto);

            assertEquals(orderDto, actual);
        }
    }

    @Nested
    class WhenGettingOrdersByUserId {
        private final int PAGE = 61;
        private final int SIZE = 65;

        @Test
        void getOrdersByUserId_shouldReturnOrderList() {
            var order = ModelFactory.createOrder();
            var orderDto = ModelFactory.toOrderDto(order);
            given(userRepository.findById(anyLong())).willReturn(Optional.ofNullable(order.getUser()));
            given(orderRepository.findOrdersByUserId(anyLong(), any())).willReturn(new PageImpl<>(Collections.singletonList(order)));
            given(orderConverter.toDto(any())).willReturn(orderDto);

            Page<OrderDto> actual = service.getOrdersByUserId(anyLong(), PAGE, SIZE);

            assertEquals(Collections.singletonList(orderDto), actual.getContent());
        }

        @Test
        void getOrdersByUserId_shouldThrowException() {
            assertThrows(PersistentException.class, () -> service.getOrdersByUserId(0, PAGE, SIZE));
        }
    }
}