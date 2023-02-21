package com.epam.esm.service.dto;

import lombok.*;

import javax.validation.constraints.Min;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class MakeOrderDto extends OrderDto {
    @Min(value = 1, message = "40001")
    private Long giftCertificateId;
    @Min(value = 1, message = "40001")
    private Long userId;
}
