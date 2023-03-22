package com.epam.esm.jwt;

import org.springframework.security.core.userdetails.UserDetails;

import java.util.Map;

public interface TokenGenerator {
    String generateToken(Map<String, Object> customClaims, UserDetails userDetails);
}
