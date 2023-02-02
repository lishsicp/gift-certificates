package com.epam.esm.dto;


import com.epam.esm.dto.group.OnPersist;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.hateoas.RepresentationModel;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class GiftCertificateDto extends RepresentationModel<GiftCertificateDto> {

    @Min(value = 1, message = "40001")
    private Long id;

    @Pattern(regexp = "[\\w\\s]{3,64}+", message = "40003")
    @NotEmpty(groups = OnPersist.class)
    private String name;

    @Pattern(regexp = "[\\w\\s-]{3,256}+", message = "40004")
    @NotEmpty(groups = OnPersist.class)
    private String description;

    @DecimalMin(value = "0.1", message = "40005")
    @Digits(integer=8, fraction=2, message = "40005")
    @NotNull(groups = OnPersist.class)
    private BigDecimal price;

    @Min(value = 1, message = "40006")
    @Max(value = 365, message = "40006")
    @NotNull(groups = OnPersist.class)
    private Long duration;

    @PastOrPresent
    private LocalDateTime createDate;

    @PastOrPresent
    private LocalDateTime lastUpdateDate;

    @NotEmpty(groups = OnPersist.class)
    private List<@Valid TagDto> tags;

}
