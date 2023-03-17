package com.epam.esm.dto;

import com.epam.esm.dto.group.OnPersist;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Pattern;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true)
public class GiftCertificateDto extends RepresentationModel<GiftCertificateDto> {

    @JsonIgnore
    private long id;

    @Pattern(regexp = "[\\w\\s-]{3,64}+", message = "40003")
    @NotEmpty(groups = OnPersist.class)
    private String name;

    @Pattern(regexp = "[\\w\\s-]{3,256}+", message = "40004")
    @NotEmpty(groups = OnPersist.class)
    private String description;

    @DecimalMin(value = "0.1", message = "40005")
    @Digits(integer = 8, fraction = 2, message = "40005")
    @NotNull(groups = OnPersist.class)
    private BigDecimal price;

    @Min(value = 1, message = "40006")
    @Max(value = 365, message = "40006")
    @NotNull(groups = OnPersist.class)
    private long duration;

    @PastOrPresent
    private LocalDateTime createDate;

    @PastOrPresent
    private LocalDateTime lastUpdateDate;

    private List<@Valid TagDto> tags;

}
