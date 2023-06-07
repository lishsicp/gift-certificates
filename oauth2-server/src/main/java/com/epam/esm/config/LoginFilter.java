//package com.epam.esm.config;
//
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//
////@Component
//public class LoginFilter extends UsernamePasswordAuthenticationFilter {
//
//    private final UserDetailsService userDetailsService;
//
//    @Autowired
//    public LoginFilter(UserDetailsService userDetailsService) {
//        this.userDetailsService = userDetailsService;
//    }
//
////    @Autowired
////    public void setAuthManager(AuthenticationManager authenticationManager) {
////        this.setAuthenticationManager(authenticationManager);
////    }
//
//    @Override
//    public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse res)
//        throws AuthenticationException {
////        final String username = req.getParameter("email");
////        final String password = req.getParameter("password");
////        final UserDetails userDetails = userDetailsService.loadUserByUsername(username);
////        if (userDetails == null) {
////            return null;
////        }
////        var authToken = UsernamePasswordAuthenticationToken.unauthenticated(userDetails, password);
////        authToken.setDetails(this.authenticationDetailsSource.buildDetails(req));
//        super.setUsernameParameter("email");
//        return super.attemptAuthentication(req, res);
//    }
//}
