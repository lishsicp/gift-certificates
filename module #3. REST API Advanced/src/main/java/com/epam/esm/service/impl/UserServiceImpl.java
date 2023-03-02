package com.epam.esm.service.impl;

import com.epam.esm.repository.UserRepository;
import com.epam.esm.entity.User;
import com.epam.esm.exception.PersistentException;
import com.epam.esm.service.UserService;
import com.epam.esm.dto.UserDto;
import com.epam.esm.dto.converter.UserConverter;
import com.epam.esm.exception.ErrorCodes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final UserConverter userConverter;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, UserConverter userConverter) {
        this.userRepository = userRepository;
        this.userConverter = userConverter;
    }


    @Override
    public Page<UserDto> getAll(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<User> users = userRepository.findAll(pageable);
        return users.map(userConverter::toDto);
    }

    @Override
    public UserDto getById(long id) throws PersistentException {
        User user = userRepository.findById(id).orElseThrow(() ->
                new PersistentException(ErrorCodes.RESOURCE_NOT_FOUND, id)
        );
        return userConverter.toDto(user);
    }

    @Override
    public UserDto save(UserDto userDto) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void delete(long id) throws PersistentException  {
        throw new UnsupportedOperationException();
    }
}
