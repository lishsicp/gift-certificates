package com.epam.esm.service;

import com.epam.esm.dto.RegisteredClientDto;
import com.epam.esm.dto.ScopesDto;

/**
 * Defines the service interface for operations related to registered clients.
 */
public interface RegisteredClientService {

    /**
     * Create a new registered client.
     *
     * @param registeredClientDto The RegisteredClientDto object containing the details of the created client.
     */
    void create(RegisteredClientDto registeredClientDto);

    /**
     * Get a registered client by client ID.
     *
     * @param clientId The ID of the client to fetch.
     * @return The RegisteredClientDto object.
     */
    RegisteredClientDto getByClientId(String clientId);

    /**
     * Update the scopes of a registered client.
     *
     * @param clientId The ID of the client to update.
     * @param scopes   The ScopesDto object containing the updated scope information.
     * @return The RegisteredClientDto object.
     */
    RegisteredClientDto updateScopesByClientId(String clientId, ScopesDto scopes);

    /**
     * Get the scopes associated with a registered client.
     *
     * @param clientId The ID of the client to retrieve the scopes for.
     * @return The ScopesDto object containing the scopes of the client with the given ID.
     */
    ScopesDto getScopesByClientId(String clientId);
}