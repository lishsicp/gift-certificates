package com.epam.esm.service;

import com.epam.esm.dto.UserDto;
import com.epam.esm.exception.PersistentException;

/**
 * This interface provides data access functionality for the {@link UserDto} entity.
 */
public interface UserService extends CRDService<UserDto> {

    UserDto getByEmail(String email) throws PersistentException;
}
