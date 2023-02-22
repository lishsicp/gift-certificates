package com.epam.esm.service.dto;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
