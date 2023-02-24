package com.epam.esm.service;


import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Order;
import com.epam.esm.entity.Tag;
import com.epam.esm.entity.User;
import com.epam.esm.repository.GiftCertificateRepository;
import com.epam.esm.repository.OrderRepository;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.repository.UserRepository;
import net.datafaker.Faker;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class DataGeneratorServiceImpl implements DataGeneratorService {

    private final TagRepository tagRepository;
    private final GiftCertificateRepository certificateRepository;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final Faker faker;

    public DataGeneratorServiceImpl(TagRepository tagRepository, GiftCertificateRepository certificateRepository, OrderRepository orderRepository, UserRepository userRepository, Faker faker) {
        this.tagRepository = tagRepository;
        this.certificateRepository = certificateRepository;
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.faker = faker;
    }

    @Override
    public void populateCertificates() {
        List<GiftCertificate> certificates = new ArrayList<>(10000);
        for (long i = 1; i <= 10000; i++) {
            LocalDateTime fakeDate = faker.date().past(30, TimeUnit.DAYS).toLocalDateTime();
            var game = faker.videoGame();
            GiftCertificate giftCertificate = GiftCertificate.builder().createDate(fakeDate)
                    .lastUpdateDate(fakeDate)
                    .name(game.title() +" Gift Certificate " + i)
                    .description("Certificate for " + game.genre())
                    .duration(faker.number().numberBetween(1L, 365L))
                    .price(BigDecimal.valueOf(faker.number().randomDouble(2, 1, 999)))
                    .build();
            List<Tag> tags = tagRepository.findAllById(
                    Stream.generate(this::numberFormOneToThousand)
                            .limit(faker.number().numberBetween(1, 10))
                            .collect(Collectors.toList()));
            giftCertificate.setTags(tags);
            certificates.add(giftCertificate);
        }
        certificateRepository.saveAll(certificates);
    }

    @Override
    public void populateTags() {
        List<Tag> tagList = new ArrayList<>(1000);
        for (int i = 1; i <= 1000; i++) {
            Tag tag = new Tag();
            tag.setId(0);
            tag.setName(faker.videoGame().genre() + " " + i);
            tagList.add(tag);
        }
        tagRepository.saveAll(tagList);
    }

    @Override
    public void populateOrders() {
        List<Order> orders = new ArrayList<>(1000);
        for (int i = 1; i <= 1000; i++) {
            LocalDateTime fakeDate = faker.date().past(30, TimeUnit.DAYS).toLocalDateTime();
            Order order = Order.builder()
                    .giftCertificate(GiftCertificate.builder().id(numberFormOneToThousand()).build())
                    .user(User.builder().id(numberFormOneToThousand()).build())
                    .purchaseDate(fakeDate)
                    .cost(BigDecimal.valueOf(faker.number().randomDouble(2, 1, 999)))
                    .build();
            orders.add(order);
        }
        orderRepository.saveAll(orders);
    }

    @Override
    public void populateUsers() {
        List<User> users = new ArrayList<>(1000);
        for (int i = 1; i <= 1000; i++) {
            User user = User.builder()
                    .name(faker.name().fullName())
                    .build();
            users.add(user);
        }
        userRepository.saveAll(users);
    }

    private long numberFormOneToThousand() {
        return faker.number().numberBetween(1, 1000);
    }
}
