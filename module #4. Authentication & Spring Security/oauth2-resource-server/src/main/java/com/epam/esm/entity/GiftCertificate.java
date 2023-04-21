package com.epam.esm.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.SequenceGenerator;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Entity(name = "gift_certificate")
@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
@Builder
public class GiftCertificate extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 2372946955532531797L;

    public static final String GIFT_CERTIFICATE_SEQ = "gift_certificate_id_seq";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = GIFT_CERTIFICATE_SEQ)
    @SequenceGenerator(name = GIFT_CERTIFICATE_SEQ, sequenceName = GIFT_CERTIFICATE_SEQ, allocationSize = 1)
    private long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "price", nullable = false)
    private BigDecimal price;

    @Column(name = "duration", nullable = false)
    private long duration;

    @Column(name = "create_date")
    private LocalDateTime createDate;

    @Column(name = "last_update_date")
    private LocalDateTime lastUpdateDate;

    @ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinTable(
        name = "gift_certificate_tag",
        joinColumns = @JoinColumn(name = "gift_certificate_id"),
        inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private List<Tag> tags;

    @Override
    public boolean equals(Object o) {
      if (this == o) {
        return true;
      }
      if (o == null || getClass() != o.getClass()) {
        return false;
      }
        GiftCertificate that = (GiftCertificate) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
