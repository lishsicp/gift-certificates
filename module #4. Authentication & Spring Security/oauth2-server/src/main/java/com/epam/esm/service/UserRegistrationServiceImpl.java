package com.epam.esm.service;

import com.epam.esm.dto.UserRegisterDto;
import com.epam.esm.entity.AuthUser;
import com.epam.esm.entity.Role;
import com.epam.esm.exception.DuplicateKeyException;
import com.epam.esm.repository.AuthUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserRegistrationServiceImpl implements UserRegistrationService {

    private final AuthUserRepository authUserRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void register(UserRegisterDto registerDto) {
        boolean existsByEmail = authUserRepository.existsByEmail(registerDto.getEmail());
        if (existsByEmail) {
            throw new DuplicateKeyException("User exist");
        }
        AuthUser authUser = AuthUser.builder()
            .email(registerDto.getEmail())
            .password(passwordEncoder.encode(registerDto.getPassword()))
            .firstname(registerDto.getFirstname())
            .lastname(registerDto.getLastname())
            .role(Role.USER)
            .build();
        authUserRepository.save(authUser);
    }

}
