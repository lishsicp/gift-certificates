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
import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
@Slf4j
class JwtAuthenticationTokenConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    private final UserRepository userRepository;
    private static final String SCOPE_PREFIX = "SCOPE_";
    private static final Set<String> USER_PERMISSION =
        Set.of("tag.read", "user.read", "certificate.read", "user.order.read", "user.order.write", "openid");

    @Override
    public AbstractAuthenticationToken convert(@NonNull Jwt jwt) {
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

    @SuppressWarnings("null")
    private Collection<GrantedAuthority> extractAuthorities(Jwt jwt) {
        JwtGrantedAuthoritiesConverter grantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        Collection<GrantedAuthority> authorities = grantedAuthoritiesConverter.convert(jwt);

        String userRole = (String) jwt.getClaims().get("role");

        if (userRole != null && userRole.equals("ROLE_USER") && !authorities.isEmpty()) {
            List<GrantedAuthority> restrictedAuthorities =
                authorities.stream()
                    .filter(a -> !USER_PERMISSION.contains(a.getAuthority().substring(SCOPE_PREFIX.length())))
                    .toList();
            authorities.removeAll(restrictedAuthorities);
        }

        authorities.add(new SimpleGrantedAuthority(userRole));

        log.debug("User JWT token authorities: {}", authorities);
        return authorities;
    }
}