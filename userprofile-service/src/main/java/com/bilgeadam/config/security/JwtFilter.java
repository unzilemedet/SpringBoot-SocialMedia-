package com.bilgeadam.config.security;

import com.bilgeadam.exception.ErrorType;
import com.bilgeadam.exception.UserProfileManagerException;
import com.bilgeadam.utility.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Optional;


public class JwtFilter extends OncePerRequestFilter {
    @Autowired
    private JwtTokenProvider jwtTokenManager;

    @Autowired
    private JwtUserDetails jwtUserDetails;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");
        System.out.println("Header' a gelen token --> " + authHeader);

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            System.out.println("Header' da substring edilmiş token --> " + token);

            Optional<Long> authId = jwtTokenManager.getIdFromToken(token);
            Optional<String> userRole = jwtTokenManager.getRoleFromToken(token);
            if (userRole.isPresent()) {
                System.out.println("Token'dan alınan rol --> " + userRole);
                try {
                    UserDetails userDetails = jwtUserDetails.loadUserByUserRole(userRole.get(), authId.get());
                    UsernamePasswordAuthenticationToken authenticationToken =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                throw new UserProfileManagerException(ErrorType.INVALID_TOKEN);
            }
        }
        filterChain.doFilter(request, response);
    }
}

