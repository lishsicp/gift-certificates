package com.epam.esm.service.impl;

import com.epam.esm.dto.UserDto;
import com.epam.esm.dto.converter.UserConverter;
import com.epam.esm.entity.User;
import com.epam.esm.exception.ErrorCodes;
import com.epam.esm.exception.PersistentException;
import com.epam.esm.repository.UserRepository;
import com.epam.esm.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserConverter userConverter;

    @Override
    public Page<UserDto> getAll(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<User> users = userRepository.findAll(pageable);
        return users.map(userConverter::toDto);
    }

    @Override
    public UserDto getById(long id) throws PersistentException {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isEmpty()) {
            throw new PersistentException(ErrorCodes.RESOURCE_NOT_FOUND, id);
        }
        return userConverter.toDto(userOptional.get());
    }

    @Override
    public UserDto getByEmail(String email) throws PersistentException {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isEmpty()) {
            throw new PersistentException(ErrorCodes.RESOURCE_NOT_FOUND, email);
        }
        return userConverter.toDto(userOptional.get());
    }

    @Override
    public UserDto save(UserDto userDto) {
        Optional<User> oUser = userRepository.findByEmail(userDto.getEmail());
        if (oUser.isPresent()) {
            throw new PersistentException(ErrorCodes.DUPLICATED_TAG, userDto.getEmail());
        }
        User savedUser = userRepository.save(userConverter.toEntity(userDto));
        return userConverter.toDto(savedUser);
    }

    @Override
    public void delete(long id) throws PersistentException {
        throw new UnsupportedOperationException();
    }
}
