package com.epam.esm.service.impl;

import com.epam.esm.entity.User;
import com.epam.esm.exception.ErrorCodes;
import com.epam.esm.exception.PersistentException;
import com.epam.esm.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) {
        Optional<User> oUser = userRepository.findByEmail(username);
        if (oUser.isEmpty()) {
            throw new PersistentException(ErrorCodes.RESOURCE_NOT_FOUND, username);
        }

        User user = oUser.get();

        return new org.springframework.security.core.userdetails.User(
            user.getEmail(),
            null,
            Collections.emptyList()
        );
    }
}
