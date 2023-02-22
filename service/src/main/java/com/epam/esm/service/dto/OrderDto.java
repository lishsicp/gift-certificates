package com.epam.esm.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = false)
public class OrderDto extends RepresentationModel<OrderDto> {

    private Long id;

    private BigDecimal cost;

    private LocalDateTime purchaseDate;

    private GiftCertificateDto giftCertificate;

    private UserDto user;
}
