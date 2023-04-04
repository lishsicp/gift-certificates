package com.epam.esm.config;

import com.epam.esm.entity.User;
import com.epam.esm.repository.UserRepository;
import lombok.RequiredArgsConstructor;
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
import java.util.Optional;

@Component
@RequiredArgsConstructor
class JwtAuthenticationTokenConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    private final UserRepository userRepository;

    @Override
    @SuppressWarnings("null")
    public AbstractAuthenticationToken convert(@NonNull Jwt jwt) {
        Optional<User> byEmail = userRepository.findByEmail(jwt.getSubject());

        if (byEmail.isEmpty()) {
            String userName = (String) jwt.getClaims().get("name");
            String userEmail = jwt.getSubject();
            User newUser = User.builder().name(userName).email(userEmail).build();
            userRepository.save(newUser);
        }

        JwtGrantedAuthoritiesConverter grantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        Collection<GrantedAuthority> authorities = grantedAuthoritiesConverter.convert(jwt);
        authorities.add(new SimpleGrantedAuthority((String) jwt.getClaims().get("role")));

        return new JwtAuthenticationToken(jwt, authorities, jwt.getSubject());
    }
}