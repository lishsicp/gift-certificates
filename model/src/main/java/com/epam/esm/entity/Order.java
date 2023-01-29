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
@NoArgsConstructor
public class Order extends BaseEntity {

    private BigDecimal cost;

    private LocalDateTime purchaseDate;

    @ManyToOne
    @JoinColumn(name = "gift_certificate_id")
    private GiftCertificate giftCertificate;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Builder
    public Order(Long id, BigDecimal cost, LocalDateTime purchaseDate, GiftCertificate giftCertificate, User user) {
        super(id);
        this.cost = cost;
        this.purchaseDate = purchaseDate;
        this.giftCertificate = giftCertificate;
        this.user = user;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof Order)) return false;

        Order order = (Order) o;

        return new EqualsBuilder()
                .appendSuper(super.equals(o))
                .append(cost, order.cost)
                .append(purchaseDate, order.purchaseDate)
                .append(giftCertificate, order.giftCertificate)
                .append(user, order.user)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .appendSuper(super.hashCode())
                .append(cost)
                .append(purchaseDate)
                .append(giftCertificate)
                .append(user)
                .toHashCode();
    }
}
