package com.epam.esm.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.security.Key;
import java.util.Date;
import java.util.function.Function;

@Component
@PropertySource(value = "classpath:signing.key.properties")
public class JwtTokenUtil implements Serializable {

    @Value("${jwt.signing.key}") // should be HS256 hex value
    private String signingKey;

    public String extractUserEmail(String token) {
        return extractClaimFromToken(token, Claims::getSubject);
    }

    public String extractUserRole(String token) {
        return extractClaimFromToken(token, claims -> claims.get("role", String.class));
    }

    public String extractName(String token) {
        return extractClaimFromToken(token, claims -> claims.get("name", String.class));
    }

    public Date extractExpirationDateFromToken(String token) {
        return extractClaimFromToken(token, Claims::getExpiration);
    }

    private Boolean isTokenExpired(String token) {
        final Date expirationDate = extractExpirationDateFromToken(token);
        return expirationDate.before(new Date());
    }

    public <T> T extractClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parserBuilder()
            .setSigningKey(getSigningKey())
            .build()
            .parseClaimsJws(token)
            .getBody();
    }

    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(signingKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public Boolean isValid(String token, String email) {
        final String username = extractUserEmail(token);
        return (username.equals(email) && !isTokenExpired(token));
    }
}
