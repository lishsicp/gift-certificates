package com.epam.esm.service;

import com.epam.esm.dto.RegisteredClientDto;
import com.epam.esm.dto.ScopesDto;

public interface RegisteredClientService {

    void create(RegisteredClientDto registeredClientDto);

    RegisteredClientDto getByClientId(String clientId);

    RegisteredClientDto updateScopesByClientId(String clientId, ScopesDto scopes);

    ScopesDto getScopesByClientId(String clientId);
}
