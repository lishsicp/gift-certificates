package com.epam.esm.web;

import com.epam.esm.dto.RegisteredClientDto;
import com.epam.esm.dto.ScopesDto;
import com.epam.esm.service.RegisteredClientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("connect/register")
@RequiredArgsConstructor
public class RegisteredClientController {

    private final RegisteredClientService registeredClientService;

    @GetMapping(path = "/{clientId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    public RegisteredClientDto getByClientId(@PathVariable String clientId) {
        return registeredClientService.getByClientId(clientId);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN')")
    public RegisteredClientDto save(@RequestBody @Valid RegisteredClientDto registeredClientDto) {
        registeredClientService.create(registeredClientDto);
        return registeredClientDto;
    }

    @PatchMapping(path = {
        "/{clientId}/scopes"}, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    public RegisteredClientDto updateScopes(@PathVariable String clientId, @Valid @RequestBody ScopesDto scopes) {
        return registeredClientService.updateScopesByClientId(clientId, scopes);
    }

    @GetMapping(path = {"/{clientId}/scopes"}, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    public ScopesDto getScopes(@PathVariable String clientId) {
        return registeredClientService.getScopesByClientId(clientId);
    }
}
