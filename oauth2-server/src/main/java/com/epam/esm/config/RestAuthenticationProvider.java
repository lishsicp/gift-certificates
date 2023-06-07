//package com.epam.esm.config;
//
//import org.springframework.security.authentication.AuthenticationProvider;
//import org.springframework.security.authentication.BadCredentialsException;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Component;
//
//import java.util.Collection;
//
//@Component
//public class RestAuthenticationProvider implements AuthenticationProvider {
//
//    private final PasswordEncoder passwordEncoder;
//    private final UserDetailsService userDetailsService;
//
//    public RestAuthenticationProvider(PasswordEncoder passwordEncoder,
//        UserDetailsService userDetailsService) {
//        this.passwordEncoder = passwordEncoder;
//        this.userDetailsService = userDetailsService;
//    }
//
//    @Override
//    public Authentication authenticate(Authentication authentication)
//        throws AuthenticationException {
//        final String username = (String) authentication.getPrincipal();
//        try {
//            final UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
//
//            final String password = passwordEncoder.encode(userDetails.getPassword());
//
//            Collection<? extends GrantedAuthority> grantedAuthorities = userDetails.getAuthorities();
//            return new UsernamePasswordAuthenticationToken(username, password, grantedAuthorities);
//        } catch (Exception ex) {
//            throw new BadCredentialsException("W");
//        }
//    }
//
//    @Override
//    public boolean supports(Class<?> authentication) {
//        return UsernamePasswordAuthenticationToken.class.equals(authentication);
//    }
//}
//
