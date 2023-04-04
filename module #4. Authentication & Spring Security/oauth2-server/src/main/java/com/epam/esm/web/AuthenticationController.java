package com.epam.esm.web;

import com.epam.esm.dto.UserRegisterDto;
import com.epam.esm.service.UserRegistrationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthenticationController {

    private final UserRegistrationService authUserRegisterService;

    @PostMapping(path = "/oauth2/register",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public UserRegisterDto register(@RequestBody @Valid UserRegisterDto registerDto) {
        authUserRegisterService.register(registerDto);
        return registerDto;
    }
}
