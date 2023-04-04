package com.epam.esm.jwt;

import com.epam.esm.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    public static final String AUTHORIZATION = "Authorization";
    public static final String BEARER = "Bearer ";
    private final JwtTokenUtil tokenUtil;
    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
        @NonNull FilterChain filterChain) throws ServletException, IOException {

        String tokenFromRequest = getTokenFromRequest(request);
        String email = tokenUtil.extractUserEmail(tokenFromRequest);

        if (tokenFromRequest != null && tokenUtil.isValid(tokenFromRequest, email)) {
            saveUserIfDoNotExist(tokenFromRequest, email);

            List<GrantedAuthority> authorityList =
                Collections.singletonList(new SimpleGrantedAuthority(tokenUtil.extractUserRole(tokenFromRequest)));
            Authentication authenticationToken = new UsernamePasswordAuthenticationToken(email, null, authorityList);
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }

        filterChain.doFilter(request, response);
    }

    private void saveUserIfDoNotExist(String token, String email) {
        Optional<com.epam.esm.entity.User> oUser = userRepository.findByEmail(email);
        if (oUser.isEmpty()) {
            var newUser = com.epam.esm.entity.User.builder()
                .name(tokenUtil.extractName(token))
                .email(email)
                .build();
            userRepository.save(newUser);
        }
    }

    private String getTokenFromRequest(HttpServletRequest request) {
        String token = null;
        String authHeader = request.getHeader(AUTHORIZATION);
        if (!StringUtils.isEmpty(authHeader) && StringUtils.startsWith(authHeader, BEARER)) {
            token = authHeader.substring(BEARER.length());
        }
        return token;
    }
}
