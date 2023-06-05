package com.epam.esm.service;

import com.epam.esm.entity.AuthUserRole;

import java.util.List;

/**
 * Defines the service interface for managing user roles.
 */
public interface UserRoleService {

    /**
     * Get user role by name.
     *
     * @param roleName The name of the role to get.
     * @return The user role with the specified name.
     */
    AuthUserRole getByName(String roleName);

    /**
     * Get all user roles.
     *
     * @return A list of all user roles.
     */
    List<AuthUserRole> getAll();

    /**
     * Create a new user role.
     *
     * @param authUserRole The user role to create.
     * @return The created user role.
     */
    AuthUserRole create(AuthUserRole authUserRole);

    /**
     * Delete a user role by name.
     *
     * @param roleName The name of the role to delete.
     */
    void deleteByName(String roleName);
}
