package com.epam.esm.config;

import com.epam.esm.entity.User;
import com.epam.esm.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.stereotype.Component;

import java.util.Collection;

/**
 * A component that converts a JWT authentication object to an AbstractAuthenticationToken object, which provides the
 * user's authentication credentials and authorities, based on the content of the JWT.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationTokenConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    private final UserRepository userRepository;

    /**
     * Converts a JWT to an AbstractAuthenticationToken object, based on the content of the JWT.
     *
     * @param jwt The JWT to be converted.
     * @return The AbstractAuthenticationToken object with the user's authentication credentials and authorities.
     */
    @Override
    public JwtAuthenticationToken convert(@NonNull Jwt jwt) {
        boolean userExists = userRepository.existsByEmail(jwt.getSubject());

        if (!userExists) {
            String userName = (String) jwt.getClaims().get("name");
            String userEmail = jwt.getSubject();
            User newUser = User.builder().name(userName).email(userEmail).build();
            userRepository.save(newUser);
            log.trace("Saving new user: {}", userEmail);
        }

        Collection<GrantedAuthority> authorities = extractAuthorities(jwt);

        return new JwtAuthenticationToken(jwt, authorities, jwt.getSubject());
    }

    /**
     * Extracts user authorities from the JWT.
     *
     * @param jwt The JWT containing user authorities.
     * @return A collection of user authorities.
     */
    @SuppressWarnings("null")
    private Collection<GrantedAuthority> extractAuthorities(Jwt jwt) {
        JwtGrantedAuthoritiesConverter grantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        Collection<GrantedAuthority> authorities = grantedAuthoritiesConverter.convert(jwt);

        String userRole = (String) jwt.getClaims().get("role");

        authorities.add(new SimpleGrantedAuthority(userRole));

        log.debug("User JWT token authorities: {}", authorities);
        return authorities;
    }
}