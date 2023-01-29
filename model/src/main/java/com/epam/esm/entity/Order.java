package com.epam.esm.entity;


import lombok.*;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "`order`")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class Order extends BaseEntity<Long> {

    private BigDecimal cost;

    private LocalDateTime purchaseDate;

    @ManyToOne
    @JoinColumn(name = "gift_certificate_id")
    private GiftCertificate giftCertificate;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Order order = (Order) o;

        return new EqualsBuilder()
                .append(id, order.id)
                .append(cost, order.cost)
                .append(purchaseDate, order.purchaseDate)
                .append(giftCertificate, order.giftCertificate)
                .append(user, order.user)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(id)
                .append(cost)
                .append(purchaseDate)
                .append(giftCertificate)
                .append(user)
                .toHashCode();
    }
}
