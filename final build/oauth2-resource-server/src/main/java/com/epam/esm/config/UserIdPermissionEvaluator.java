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

/**
 * This class implements the {@link PermissionEvaluator} interface and provides permission checking for user IDs.
 */
@Component
@RequiredArgsConstructor
public class UserIdPermissionEvaluator implements PermissionEvaluator {

    private final UserRepository userRepository;

    /**
     * @inheritDoc Always throws an UnsupportedOperationException.
     */
    @Override
    public boolean hasPermission(Authentication auth, Object entity, Object permission) {
        throw new UnsupportedOperationException();
    }

    /**
     * Retrieves the user ID associated with the token and compares it with the target ID passed as an argument.
     *
     * @param authentication the authentication object for the current user.
     * @param targetId       the target ID to compare with the user ID.
     * @param targetType     the type of the target object.
     * @param permission     the permission string to check.
     * @return true if the user ID matches the target ID, false otherwise.
     */
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
