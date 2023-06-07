package com.epam.esm.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "gift_certificate")
@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
@Builder
public class GiftCertificate extends BaseEntity {

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