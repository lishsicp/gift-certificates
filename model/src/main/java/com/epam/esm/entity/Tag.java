package com.epam.esm.entity;

import lombok.*;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "tag")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Tag extends BaseEntity {

  private String name;

  @Builder
  public Tag(Long id, String name) {
    super(id);
    this.name = name;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;

    if (!(o instanceof Tag)) return false;

    Tag tag = (Tag) o;

    return new EqualsBuilder().appendSuper(super.equals(o)).append(name, tag.name).isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37).appendSuper(super.hashCode()).append(name).toHashCode();
  }
}
