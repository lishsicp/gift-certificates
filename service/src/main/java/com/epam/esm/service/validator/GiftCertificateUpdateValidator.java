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

    public static void validate(GiftCertificate giftCertificate) throws IncorrectUpdateValueException {
        if (giftCertificate.getName() != null) {
            String name = giftCertificate.getName();
            if (name.isBlank() ||
                    !Pattern.compile("[\\w\\s]{2,128}").matcher(name).matches()) {
                throw new IncorrectUpdateValueException(BAD_CERTIFICATE_NAME);
            }
        }
        if (giftCertificate.getDescription() != null) {
            String description = giftCertificate.getDescription();
            if (description.isBlank() ||
                    !Pattern.compile("[\\w\\s]{2,512}").matcher(description).matches()) {
                throw new IncorrectUpdateValueException(BAD_CERTIFICATE_DESCRIPTION);
            }
        }
        if (giftCertificate.getPrice() != null) {
            BigDecimal price = giftCertificate.getPrice();
            if (price == null || price.scale() > MAX_SCALE
                    || price.compareTo(MIN_PRICE) < 0 || price.compareTo(MAX_PRICE) > 0) {
                throw new IncorrectUpdateValueException(BAD_CERTIFICATE_PRICE);
            }
        }
        if (giftCertificate.getDuration() > 0) {
            int duration = giftCertificate.getDuration();
            if (duration < 1 || duration > 365) {
                throw new IncorrectUpdateValueException(BAD_CERTIFICATE_DURATION);
            }
        }
        validateListOfTags(giftCertificate.getTags());
    }

    private static void validateListOfTags(List<Tag> tags) throws IncorrectUpdateValueException {
        if (tags == null || tags.isEmpty()) return;
        for (Tag tag : tags) {
            if (tag.getName().isBlank() || !Pattern.compile("[\\w\\s]{2,128}").matcher(tag.getName()).matches()) {
                throw new IncorrectUpdateValueException(BAD_TAG_NAME);
            }
        }
    }
}
