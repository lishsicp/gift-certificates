package com.epam.esm.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
public class GiftCertificate implements Entity {

  private long id;

  @NotBlank
  @Pattern(regexp = "[\\w\\s]{2,128}")
  private String name;

  @NotBlank
  @Pattern(regexp = ".{2,512}")
  private String description;

  @Digits(integer = 7, fraction = 2)
  private BigDecimal price;

  @Positive
  private int duration;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
  private LocalDateTime createDate;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
  private LocalDateTime lastUpdateDate;

  private List<@Valid Tag> tags;

}
