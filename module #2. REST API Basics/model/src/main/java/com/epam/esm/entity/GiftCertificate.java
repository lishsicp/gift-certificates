package com.epam.esm.entity;

import com.epam.esm.entity.group.OnPersist;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GiftCertificate implements Entity {

  private long id;

  @NotEmpty(groups = OnPersist.class)
  @Pattern(regexp = "[\\w\\s]{2,128}", message = "40003")
  private String name;

  @NotEmpty(groups = OnPersist.class)
  @Pattern(regexp = ".{2,512}", message = "40004")
  private String description;

  @NotNull(groups = OnPersist.class)
  @DecimalMin(value = "0.1", message = "40005")
  @Digits(integer = 7, fraction = 2, message = "40005")
  private BigDecimal price;

  @NotNull(groups = OnPersist.class)
  @Min(value = 1, message = "40006")
  @Max(value = 365, message = "40006")
  private Integer duration;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
  private LocalDateTime createDate;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
  private LocalDateTime lastUpdateDate;

  private List<@Valid Tag> tags;
}
