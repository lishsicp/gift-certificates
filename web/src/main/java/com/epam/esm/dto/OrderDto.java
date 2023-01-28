package com.epam.esm.dto;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.User;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class OrderDto {

    private Long id;

    private BigDecimal cost;

    private LocalDateTime purchaseDate;

    private GiftCertificate giftCertificate;

    private User user;
}
