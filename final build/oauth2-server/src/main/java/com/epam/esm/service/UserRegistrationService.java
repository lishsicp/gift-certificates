package com.epam.esm.service;

import com.epam.esm.dto.UserRegistrationDto;

/**
 * Defines the service interface for registering new user.
 */
public interface UserRegistrationService {

    /**
     * Registers a user with provided user registration details.
     *
     * @param userRegistrationDto the user registration details to register.
     */
    void register(UserRegistrationDto userRegistrationDto);
}