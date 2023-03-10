package com.epam.esm.util;

import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.dto.OrderDto;
import com.epam.esm.dto.TagDto;
import com.epam.esm.dto.UserDto;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Order;
import com.epam.esm.entity.Tag;
import com.epam.esm.entity.User;
import lombok.experimental.UtilityClass;
import org.modelmapper.ModelMapper;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicLong;

@UtilityClass
public class ModelFactory {

    private final ModelMapper modelMapper = new ModelMapper();

    private final AtomicLong counter = new AtomicLong();

    private final LocalDateTime DATETIME = LocalDateTime.parse("2023-02-17T10:48:40.303950");

    public long generateId() {
        return counter.incrementAndGet();
    }

    public Tag createNewTag() {
        long id = generateId();
        return Tag.builder().name("tag" + id).build();
    }

    public Tag createTag() {
        long id = generateId();
        return Tag.builder().id(id).name("tag" + id).build();
    }

    public TagDto toTagDto(Tag tag) {
        return modelMapper.map(tag, TagDto.class);
    }

    public GiftCertificate createGiftCertificate() {
        long id = generateId();
        return getGiftCertificateBuilder(id)
                .build();
    }

    public GiftCertificate createNewGiftCertificate() {
        long id = generateId();
        return getGiftCertificateBuilder(id)
                .id(0)
                .build();
    }

    private GiftCertificate.GiftCertificateBuilder getGiftCertificateBuilder(long num) {
        return GiftCertificate.builder()
                .id(num)
                .name("gift" + num)
                .description("description" + num)
                .price(new BigDecimal("10.99"))
                .duration(60L)
                .createDate(DATETIME)
                .lastUpdateDate(DATETIME);
    }

    public GiftCertificateDto toGiftCertificateDto(GiftCertificate giftCertificate) {
        return modelMapper.map(giftCertificate, GiftCertificateDto.class);
    }

    public User createUser() {
        long id = generateId();
        return User.builder().id(id).name("user" + id).email("user" + id + "@at.test").build();
    }

    public User createNewUser() {
        long id = generateId();
        return User.builder().name("user" + id).email("user" + id + "@at.test").build();
    }

    public UserDto toUserDto(User user) {
        return modelMapper.map(user, UserDto.class);
    }

    public Order createOrder() {
        long id = generateId();
        return Order.builder()
                .id(id)
                .cost(new BigDecimal("10.99"))
                .purchaseDate(DATETIME)
                .user(createUser())
                .giftCertificate(createGiftCertificate())
                .build();
    }

    public Order createNewOrder() {
        return Order.builder()
                .cost(new BigDecimal("10.99"))
                .purchaseDate(DATETIME)
                .build();
    }

    public OrderDto toOrderDto(Order order) {
        return modelMapper.map(order, OrderDto.class);
    }
}
