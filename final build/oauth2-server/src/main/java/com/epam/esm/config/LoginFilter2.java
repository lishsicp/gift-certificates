//package com.epam.esm.config;
//
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.NonNull;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.stereotype.Component;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import java.io.IOException;
//
//@Component
//public class LoginFilter2 extends OncePerRequestFilter {
//
//    private final UserDetailsService userDetailsService;
//
//    @Autowired
//    public LoginFilter2(UserDetailsService userDetailsService) {
//        this.userDetailsService = userDetailsService;
//    }
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain)
//        throws ServletException, IOException {
//
//        final String username = request.getParameter("email");
//        final String password = request.getParameter("password");
//
//        if (username != null && password != null) {
//
//            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
//
//            Authentication authentication =
//                new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities());
//            SecurityContextHolder.getContext().setAuthentication(authentication);
//        }
//
//        filterChain.doFilter(request, response);
//    }
//}
