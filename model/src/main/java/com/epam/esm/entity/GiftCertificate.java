package com.epam.esm.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
public class GiftCertificate {

  @NonNull
  private long id;

  @NonNull
  private String name;

  private String description;

  @NonNull
  private BigDecimal price;

  private long duration;

  @NonNull
  private String createDate;

  @NonNull
  private String lastUpdateDate;

  private List<Tag> tags;

}
