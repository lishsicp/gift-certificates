package com.epam.esm.service;

import com.epam.esm.service.dto.UserDto;

/**
 * This interface provides data access functionality for the User entity. It extends the CRDService interface, which
 * defines the basic CRD operations for a service in the business layer, so this
 * interface inherits those methods.
 */
public interface UserService extends CRDService<UserDto> {
    // No additional methods defined in this interface
}
