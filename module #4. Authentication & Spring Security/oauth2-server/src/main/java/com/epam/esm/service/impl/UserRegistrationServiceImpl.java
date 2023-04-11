package com.epam.esm.service.impl;

import com.epam.esm.dto.UserRegistrationDto;
import com.epam.esm.entity.AuthUser;
import com.epam.esm.exception.DuplicateKeyException;
import com.epam.esm.exception.EntityNotFoundException;
import com.epam.esm.repository.AuthUserRepository;
import com.epam.esm.repository.AuthUserRoleRepository;
import com.epam.esm.service.UserRegistrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserRegistrationServiceImpl implements UserRegistrationService {

    private final AuthUserRoleRepository authUserRoleRepository;
    private final AuthUserRepository authUserRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void register(UserRegistrationDto userRegistrationDto) {
        boolean existsByEmail = authUserRepository.existsByEmail(userRegistrationDto.getEmail());
        if (existsByEmail) {
            throw new DuplicateKeyException("User exist");
        }
        var role = authUserRoleRepository.findByName("USER")
            .orElseThrow(() -> new EntityNotFoundException("Role do not exist"));
        AuthUser authUser = AuthUser.builder()
            .email(userRegistrationDto.getEmail())
            .password(passwordEncoder.encode(userRegistrationDto.getPassword()))
            .firstname(userRegistrationDto.getFirstname())
            .lastname(userRegistrationDto.getLastname())
            .role(role)
            .build();
        authUserRepository.save(authUser);
    }

}
