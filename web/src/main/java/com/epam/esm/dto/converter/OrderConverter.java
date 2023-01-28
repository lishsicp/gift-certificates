package com.epam.esm.dto.converter;

import com.epam.esm.dto.MakeOrderDto;
import com.epam.esm.dto.OrderDto;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Order;
import com.epam.esm.entity.User;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class OrderConverter extends ModelDtoConverter<Order, OrderDto> {

    protected OrderConverter(ModelMapper modelMapper) {
        super(modelMapper);
    }

    @Override
    public Order toEntity(OrderDto orderDto) {
        MakeOrderDto dto = (MakeOrderDto) orderDto;
        Order o = new Order();
        GiftCertificate certificate = new GiftCertificate();
        certificate.setId(dto.getGiftCertificateId());
        o.setGiftCertificate(certificate);
        User user = new User();
        user.setId(dto.getUserId());
        o.setUser(user);
        return o;
    }

    @Override
    public OrderDto toDto(Order order) {
        return modelMapper.map(order, OrderDto.class);
    }
}
