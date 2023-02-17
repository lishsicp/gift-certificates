package com.epam.esm.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GiftCertificate implements Entity {

  private long id;

  @NotBlank(message = "error.blank")
  @Pattern(regexp = "[\\w\\s]{2,128}")
  private String name;

  @NotBlank(message = "error.blank")
  @Pattern(regexp = ".{2,512}")
  private String description;

  @Digits(integer = 7, fraction = 2, message = "40005")
  private BigDecimal price;

  @Positive(message = "40006")
  private int duration;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
  private LocalDateTime createDate;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
  private LocalDateTime lastUpdateDate;

  private List<@Valid Tag> tags;

}
