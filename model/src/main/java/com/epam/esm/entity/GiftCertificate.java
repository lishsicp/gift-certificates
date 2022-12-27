package com.epam.esm.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
public class GiftCertificate implements Entity {

  private long id;

  @NonNull
  private String name;

  private String description;

  @NonNull
  private BigDecimal price;

  private long duration;

  @NonNull
  private LocalDateTime createDate;

  @NonNull
  private LocalDateTime lastUpdateDate;

  private List<Tag> tags;

}
