package com.epam.esm.dto;


import com.fasterxml.jackson.annotation.JsonFilter;
import lombok.Data;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class GiftCertificateDto {

    private Long id;

    private String name;

    private String description;

    private BigDecimal price;

    private long duration;

    private LocalDateTime createDate;

    private LocalDateTime lastUpdateDate;

    private List<TagDto> tags;

}
