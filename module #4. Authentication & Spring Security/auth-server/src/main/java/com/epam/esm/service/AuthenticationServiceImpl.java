package com.epam.esm.service;

import com.epam.esm.dto.AuthenticationRequest;
import com.epam.esm.dto.AuthenticationResponse;
import com.epam.esm.dto.RegisterUserRequest;
import com.epam.esm.entity.AuthUser;
import com.epam.esm.entity.Role;
import com.epam.esm.exception.CustomAuthenticationException;
import com.epam.esm.jwt.TokenGenerator;
import com.epam.esm.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;
    private final TokenGenerator tokenGenerator;
    private final PasswordEncoder passwordEncoder;

    @Override
    public AuthenticationResponse register(RegisterUserRequest userRequest) {

        if (userRepository.existsByEmail(userRequest.getEmail())) {
            throw new CustomAuthenticationException("User exist");
        }

        var user = AuthUser.builder()
            .name(userRequest.getName())
            .email(userRequest.getEmail())
            .password(passwordEncoder.encode(userRequest.getPassword()))
            .role(Role.USER)
            .build();

        userRepository.save(user);
        var jwtToken = tokenGenerator.generateToken(addUserClaims(user), user);
        return AuthenticationResponse.builder().token(jwtToken).build();
    }

    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest) {
        AuthUser user = userRepository.findByEmail(authenticationRequest.getEmail())
            .orElseThrow(() -> new CustomAuthenticationException("User not found"));

        String jwtToken = tokenGenerator.generateToken(addUserClaims(user), user);
        return AuthenticationResponse.builder().token(jwtToken).build();
    }

    private Map<String, Object> addUserClaims(AuthUser user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("name", user.getName());
        claims.put("role", "ROLE_" + user.getRole().name());
        return claims;
    }
}
