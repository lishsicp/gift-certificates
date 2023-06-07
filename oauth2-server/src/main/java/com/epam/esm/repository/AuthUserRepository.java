package com.epam.esm.repository;

import com.epam.esm.entity.AuthUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for managing AuthUser objects.
 */
@Repository
public interface AuthUserRepository extends JpaRepository<AuthUser, Long> {

    /**
     * Find the AuthUser object with the specified email.
     *
     * @param email The email of the AuthUser to find.
     * @return an optional AuthUser object
     */
    Optional<AuthUser> findByEmail(String email);

    /**
     * Check if an AuthUser object with the specified email exists.
     *
     * @param email The email of the AuthUser to check for existence.
     * @return boolean True if an AuthUser object with the specified email exists, false otherwise.
     */
    boolean existsByEmail(String email);
}