package com.epam.esm.config;

import com.epam.esm.entity.User;
import com.epam.esm.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserIdPermissionEvaluator implements PermissionEvaluator {

    private final UserRepository userRepository;

    @Override
    public boolean hasPermission(Authentication auth,
        Object entity, Object permission) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType,
        Object permission) {
        if (authentication instanceof JwtAuthenticationToken token) {
            Optional<User> user = userRepository.findByEmail(token.getName());
            return user.isPresent() && Objects.equals(user.get().getId(), targetId);
        }
        return false;
    }
}
