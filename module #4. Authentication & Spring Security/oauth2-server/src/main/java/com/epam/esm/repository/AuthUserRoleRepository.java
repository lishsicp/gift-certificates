package com.epam.esm.repository;

import com.epam.esm.entity.AuthUserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for managing authentication user roles.
 */
@Repository
public interface AuthUserRoleRepository extends JpaRepository<AuthUserRole, Long> {

    /**
     * Find an authentication user role by name.
     *
     * @param name the name of the authentication user role
     * @return an optional AuthUserRole object
     */
    Optional<AuthUserRole> findByName(String name);

    /**
     * Delete an authentication user role by name.
     *
     * @param roleName the name of the authentication user role to delete
     */
    void deleteByName(String roleName);
}