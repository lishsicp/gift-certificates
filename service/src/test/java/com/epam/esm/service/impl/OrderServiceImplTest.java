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
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.when;

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
    void findById_shouldReturnOrder() {
        given(orderRepository.findById(anyLong())).willReturn(Optional.ofNullable(order));
        given(orderConverter.toDto(any())).willReturn(orderDto);

        OrderDto actual = service.getById(anyLong());

        assertEquals(orderDto, actual);
    }

    @Test
    void findById_shouldThrowException_whenNonExistentId() {
        Long userId = order.getId();

        assertThrows(PersistentException.class, () -> service.getById(userId));
    }

    @Nested
    class WhenSaving {

        @Test
        void save_shouldThrowException_whenCertificateIsEmpty() {
            assertThrows(PersistentException.class, () -> service.save(orderDto));
        }

        @Test
        void save_shouldThrowException_whenUserIsEmpty() {
            assertThrows(PersistentException.class, () -> service.save(orderDto));
        }

        @Test
        void save_shouldSave() {
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
            given(userRepository.findById(anyLong())).willReturn(Optional.ofNullable(order.getUser()));
            given(orderRepository.findOrdersByUserId(anyLong(), any())).willReturn(new PageImpl<>(Collections.singletonList(order)));
            given(orderConverter.toDto(any())).willReturn(orderDto);

            Page<OrderDto> actual = service.getOrdersByUserId(anyLong(), PAGE, SIZE);

            assertEquals(Collections.singletonList(orderDto), actual.getContent());
        }

        @Test
        void getOrdersByUserId_shouldThrowException() {
            Long id = order.getId();

            assertThrows(PersistentException.class, () -> service.getOrdersByUserId(id, PAGE, SIZE));
        }

    }
}