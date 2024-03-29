package com.epam.esm.web;

import com.epam.esm.dto.UserRegistrationDto;
import com.epam.esm.service.UserRegistrationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * A controller for registering a new user.
 */
@RestController
@RequiredArgsConstructor
public class UserRegistrationController {

    private final UserRegistrationService authUserRegisterService;

    /**
     * Registers a new user.
     *
     * @param registerDto DTO containing details of user to be registered
     * @return DTO of registered user
     */
    @PostMapping(path = "/oauth2/register", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public UserRegistrationDto register(@RequestBody @Valid UserRegistrationDto registerDto) {
        authUserRegisterService.register(registerDto);
        return registerDto;
    }
}
