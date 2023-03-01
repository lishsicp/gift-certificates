package com.epam.esm.util;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import lombok.experimental.UtilityClass;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@UtilityClass
public class ModelFactory {

    private final AtomicLong counter = new AtomicLong(10);
    private final LocalDateTime DATETIME = LocalDateTime.parse("2023-02-17T10:48:40.303950");

    public GiftCertificate createGiftCertificate(long num, long... tagIds) {
        List<Tag> tags = createTagList(tagIds.length > 0 ? tagIds : null);
        return getGiftCertificateBuilder(num)
                .tags(tags)
                .build();
    }

    public GiftCertificate createGiftCertificate() {
        long id = generateId();
        return getGiftCertificateBuilder(id)
                .build();
    }

    private GiftCertificate.GiftCertificateBuilder getGiftCertificateBuilder(long num) {
        return GiftCertificate.builder()
                .id(num).name("gift" + num)
                .description("description" + num)
                .price(new BigDecimal("10.00"))
                .duration(60)
                .createDate(DATETIME)
                .lastUpdateDate(DATETIME);
    }

    public Tag createTag(long num) {
        return Tag.builder().id(num).name("tag" + num).build();
    }

    public Tag createTag() {
        long id = generateId();
        return Tag.builder().id(id).name("tag" + id).build();
    }

    public List<Tag> createTagList(long... tagIds) {
        if (tagIds == null || tagIds.length <= 0) {
            return null;
        }
        return Arrays
                .stream(tagIds)
                .mapToObj(ModelFactory::createTag)
                .collect(Collectors.toList());
    }

    public long generateId() {
        return counter.incrementAndGet();
    }
}
