package com.epam.esm.service.validator;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.service.exception.IncorrectUpdateValueException;

import java.math.BigDecimal;
import java.util.List;
import java.util.regex.Pattern;

public class GiftCertificateUpdateValidator {
    private static final int MAX_SCALE = 2;
    private static final BigDecimal MAX_PRICE = new BigDecimal("9999999.99");
    private static final BigDecimal MIN_PRICE = new BigDecimal("0.01");

    private static final int BAD_TAG_NAME = 40002;
    private static final int BAD_CERTIFICATE_NAME = 40003;
    private static final int BAD_CERTIFICATE_DESCRIPTION = 40004;
    private static final int BAD_CERTIFICATE_PRICE = 40005;
    private static final int BAD_CERTIFICATE_DURATION = 40006;
    private static final int BAD_CERTIFICATE_EMPTY = 40009;

    public static void validate(GiftCertificate giftCertificate) {
        checkIfEmpty(giftCertificate);
        if (giftCertificate.getId() <= 0) {
            throw new IncorrectUpdateValueException(40001);
        }
        if (giftCertificate.getName() != null) {
            checkName(giftCertificate.getName());
        }
        if (giftCertificate.getDescription() != null) {
            checkDescription(giftCertificate.getDescription());
        }
        if (giftCertificate.getPrice() != null) {
            checkPrice(giftCertificate.getPrice());
        }
        if (giftCertificate.getDuration() > 0) {
            checkDuration(giftCertificate.getDuration());
        }
        validateListOfTags(giftCertificate.getTags());
    }

    private static void checkDuration(int duration) {
        if (duration < 1 || duration > 365) {
            throw new IncorrectUpdateValueException(BAD_CERTIFICATE_DURATION);
        }
    }

    private static void checkPrice(BigDecimal price) {
        if (price != null && (price.scale() > MAX_SCALE
                || price.compareTo(MIN_PRICE) < 0 || price.compareTo(MAX_PRICE) > 0)) {
            throw new IncorrectUpdateValueException(BAD_CERTIFICATE_PRICE);
        }
    }

    private static void checkDescription(String description) {
        if (description != null && (description.isBlank() || !Pattern.compile("[\\w\\s]{2,512}").matcher(description).matches())) {
            throw new IncorrectUpdateValueException(BAD_CERTIFICATE_DESCRIPTION);
        }
    }

    private static void checkName(String name) {
        if (name != null && (name.isBlank() || !Pattern.compile("[\\w\\s]{2,128}").matcher(name).matches())) {
            throw new IncorrectUpdateValueException(BAD_CERTIFICATE_NAME);
        }
    }

    private static void checkIfEmpty(GiftCertificate item) {
        if (item.getName() == null
                && item.getDescription() == null
                && item.getPrice() == null
                && item.getDuration() < 1
                && item.getTags() == null) {
            throw new IncorrectUpdateValueException(BAD_CERTIFICATE_EMPTY);
        }
    }

    private static void validateListOfTags(List<Tag> tags) {
        if (tags == null || tags.isEmpty()) return;
        for (Tag tag : tags) {
            if (tag.getName().isBlank() || !Pattern.compile("[\\w\\s]{2,128}").matcher(tag.getName()).matches()) {
                throw new IncorrectUpdateValueException(BAD_TAG_NAME);
            }
        }
    }
}
