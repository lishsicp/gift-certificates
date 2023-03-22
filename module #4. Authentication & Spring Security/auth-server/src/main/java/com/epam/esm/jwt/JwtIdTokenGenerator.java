package com.epam.esm.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Map;

@Component
@PropertySource(value = "classpath:signing.key.properties")
public class JwtIdTokenGenerator implements Serializable, TokenGenerator {

    @Value("${jwt.signing.key}") // should be HS256 hex value
    private String signingKey;

    @Value("${jwt.expiration}")
    private int expiration;

    @Value("${jwt.issuer}")
    private String issuer;

    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(signingKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(Map<String, Object> customClaims, UserDetails userDetails) {
        Date issuedAt = Date.from(LocalDateTime.now()
            .atZone(ZoneId.systemDefault())
            .toInstant());

        Date expiresAt = Date.from(issuedAt.toInstant()
            .plus(expiration, ChronoUnit.HOURS));

        return Jwts.builder()
            .addClaims(customClaims)
            .setSubject(userDetails.getUsername())
            .setIssuedAt(issuedAt)
            .setExpiration(expiresAt)
            .setIssuer(issuer)
            .signWith(getSigningKey(), SignatureAlgorithm.HS256)
            .compact();
    }
}
