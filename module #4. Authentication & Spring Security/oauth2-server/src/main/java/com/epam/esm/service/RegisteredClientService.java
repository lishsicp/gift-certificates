package com.epam.esm.service;

import com.epam.esm.dto.RegisteredClientDto;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;

public interface RegisteredClientService {
    void create(RegisteredClientDto registeredClientDto);
    RegisteredClient getByClientId(String clientId);
}
