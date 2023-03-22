package com.epam.esm.contoller;

import com.epam.esm.dto.AuthenticationRequest;
import com.epam.esm.dto.AuthenticationResponse;
import com.epam.esm.dto.RegisterUserRequest;
import com.epam.esm.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public AuthenticationResponse register(@RequestBody RegisterUserRequest userRequest) {
        return authenticationService.register(userRequest);
    }

    @PostMapping
    public AuthenticationResponse authenticate(@RequestBody AuthenticationRequest authenticationRequest) {
        return authenticationService.authenticate(authenticationRequest);
    }
}
