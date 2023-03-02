package com.epam.esm.dto.converter;

import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.entity.GiftCertificate;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class GiftCertificateConverter extends ModelDtoConverter<GiftCertificate, GiftCertificateDto> {

    public GiftCertificateConverter(ModelMapper modelMapper) {
        super(modelMapper);
    }

    @Override
    public GiftCertificate toEntity(GiftCertificateDto giftCertificateDto) {
        return modelMapper.map(giftCertificateDto, GiftCertificate.class);
    }

    @Override
    public GiftCertificateDto toDto(GiftCertificate giftCertificate) {
        return modelMapper.map(giftCertificate, GiftCertificateDto.class);
    }
}
