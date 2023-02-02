package com.epam.esm.dao.impl;

import com.epam.esm.config.TestDaoConfig;
import com.epam.esm.dao.GenericDao;
import com.epam.esm.dao.GiftCertificateDao;
import com.epam.esm.dao.OrderDao;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Order;
import com.epam.esm.entity.Tag;
import com.epam.esm.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.ConfigDataApplicationContextInitializer;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import javax.transaction.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest(classes = TestDaoConfig.class)
@ContextConfiguration(initializers = ConfigDataApplicationContextInitializer.class)
@ActiveProfiles("test")
@Transactional
class OrderDaoImplTest {

    @Autowired
    private OrderDao orderDao;
    @Autowired
    private GiftCertificateDao giftCertificateDao;

    private static final Pageable PAGE_REQUEST = PageRequest.of(0, 10);
    private static final Long NON_EXISTED_ORDER_ID = 999L;

    private static final GiftCertificate CERTIFICATE_1 = GiftCertificate.builder()
            .id(1L).name("GiftCertificate1").description("Description")
            .price(new BigDecimal("500.00")).duration(60L)
            .createDate(LocalDateTime.parse("2022-12-15T11:43:33"))
            .lastUpdateDate((LocalDateTime.parse("2022-12-15T11:43:33")))
            .tags(Collections.singletonList(Tag.builder().id(2L).name("tag2").build()))
            .build();

    private static final GiftCertificate CERTIFICATE_2 = GiftCertificate.builder()
            .id(2L).name("GiftCertificate2").description("Description")
            .price(new BigDecimal("200.00")).duration(1L)
            .createDate(LocalDateTime.parse("2023-01-25T13:56:30"))
            .lastUpdateDate((LocalDateTime.parse("2023-01-25T13:56:30")))
            .tags(Arrays.asList(Tag.builder().id(2L).name("tag2").build(), Tag.builder().id(4L).name("tag4").build()))
            .build();

    private static final GiftCertificate CERTIFICATE_3 = GiftCertificate.builder()
            .id(3L).name("GiftCertificate3").description("Description")
            .price(new BigDecimal("300.00")).duration(90L)
            .createDate(LocalDateTime.parse("2023-01-30T09:08:56"))
            .lastUpdateDate((LocalDateTime.parse("2023-01-30T09:08:56")))
            .tags(Arrays.asList(Tag.builder().id(2L).name("tag2").build(), Tag.builder().id(4L).name("tag4").build()))
            .build();

    private static final User USER_1 = User.builder().id(1L).name("User1").build();

    private static final Order ORDER_1 = Order.builder().id(1L)
            .purchaseDate(LocalDateTime.parse("2023-01-29T10:42:20.57"))
            .cost(new BigDecimal("200.00")).giftCertificate(CERTIFICATE_1).user(USER_1).build();
    private static final Order ORDER_2 = Order.builder().id(2L)
            .purchaseDate(LocalDateTime.parse("2023-01-29T10:42:20.57"))
            .cost(new BigDecimal("500.00")).giftCertificate(CERTIFICATE_2).user(USER_1).build();
    private static final Order ORDER_3 = Order.builder().id(3L)
            .purchaseDate(LocalDateTime.parse("2023-01-29T10:42:20.57"))
            .cost(new BigDecimal("300.00")).giftCertificate(CERTIFICATE_3).user(USER_1).build();


    @Test
    void testFindByIdAShouldReturnOne() {
        Optional<Order> order = orderDao.findById(ORDER_1.getId());
        assertTrue(order.isPresent());
        assertEquals(ORDER_1, order.get());
    }

    @Test
    void testFindByIdAShouldBeEmpty() {
        Optional<Order> order = orderDao.findById(NON_EXISTED_ORDER_ID);
        assertTrue(order.isEmpty());
    }

    @Test
    void testSaveShouldSaveAndGenerateId() {
        ORDER_1.setId(null);
        Order savedOrder = orderDao.save(ORDER_1);
        assertTrue(savedOrder.getId() > 0);
    }

    @Test
    void testOrderCostShouldNotBeChangedIfThePriceOfTheGiftCertificateIsChanged() {
        Optional<GiftCertificate> optionalGiftCertificate = giftCertificateDao.findById(CERTIFICATE_1.getId());
        optionalGiftCertificate.ifPresent((a) -> a.setPrice(new BigDecimal("1000")));
        assertTrue(optionalGiftCertificate.isPresent());
        giftCertificateDao.update(optionalGiftCertificate.get());
        Optional<Order> order = orderDao.findById(ORDER_1.getId());
        assertTrue(order.isPresent());
        assertEquals(order.get().getCost(), ORDER_1.getCost());
    }

    @Test
    void findOrdersByUserIdShouldReturnThree() {
        List<Order> actual = orderDao.findOrdersByUserId(USER_1.getId(), PAGE_REQUEST);
        List<Order> expected = Arrays.asList(ORDER_1, ORDER_2, ORDER_3);
        assertEquals(expected, actual);
    }
}