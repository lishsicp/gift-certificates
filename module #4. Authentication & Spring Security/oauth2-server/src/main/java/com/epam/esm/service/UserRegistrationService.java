package com.epam.esm.service;

import com.epam.esm.dto.UserRegisterDto;

public interface UserRegistrationService {

    void register(UserRegisterDto registerDto);
}
