package com.epam.esm.web;

import com.epam.esm.entity.AuthUserRole;
import com.epam.esm.service.UserRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * A controller for managing managing user roles.
 */
@RestController
@RequestMapping("oauth2/roles")
@RequiredArgsConstructor
public class UserRoleController {

    private final UserRoleService userRoleService;

    /**
     * Creates a new user role.
     *
     * @param role The role to create.
     * @return The created role.
     */
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN')")
    public AuthUserRole create(@RequestBody AuthUserRole role) {
        return userRoleService.create(role);
    }

    /**
     * Gets a list of all user roles.
     *
     * @return A list of all user roles.
     */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<AuthUserRole> getAll() {
        return userRoleService.getAll();
    }

    /**
     * Gets a user role by name.
     *
     * @param roleName The name of the role to get.
     * @return The user role with the specified name.
     */
    @GetMapping("/{roleName}")
    @PreAuthorize("hasRole('ADMIN')")
    public AuthUserRole getByName(@PathVariable String roleName) {
        return userRoleService.getByName(roleName);
    }

    /**
     * Deletes a user role by name.
     *
     * @param roleName The name of the role to delete.
     * @return A response indicating the success or failure of the delete operation.
     */
    @DeleteMapping("/{roleName}")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<AuthUserRole> deleteByName(@PathVariable String roleName) {
        userRoleService.deleteByName(roleName);
        return ResponseEntity.noContent().build();
    }
}
