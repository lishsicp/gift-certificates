package com.epam.esm.dto.converter;

import com.epam.esm.dto.UserDto;
import com.epam.esm.entity.User;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserConverter extends ModelDtoConverter<User, UserDto> {

    @Autowired
    protected UserConverter(ModelMapper modelMapper) {
        super(modelMapper);
    }

    @Override
    public User toEntity(UserDto userDto) {
        return modelMapper.map(userDto, User.class);
    }

    @Override
    public UserDto toDto(User user) {
        return modelMapper.map(user, UserDto.class);
    }
}
