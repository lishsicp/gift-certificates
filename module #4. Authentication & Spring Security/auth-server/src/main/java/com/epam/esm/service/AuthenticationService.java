package com.epam.esm.service;

import com.epam.esm.dto.AuthenticationRequest;
import com.epam.esm.dto.AuthenticationResponse;
import com.epam.esm.dto.RegisterUserRequest;

public interface AuthenticationService {
    AuthenticationResponse register(RegisterUserRequest userRequest);
    AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest);
}
