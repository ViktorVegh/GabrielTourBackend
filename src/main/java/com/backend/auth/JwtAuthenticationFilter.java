package com.backend.auth;

import com.backend.entity.Person;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtHelper jwtHelper;

    public JwtAuthenticationFilter(JwtHelper jwtHelper) {
        this.jwtHelper = jwtHelper;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws IOException, jakarta.servlet.ServletException {

        String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7); // Remove "Bearer " prefix
            Person person = jwtHelper.validateTokenAndRetrievePerson(token); // Validate token and get the person
            String role = jwtHelper.getRoleFromToken(token); // Extract role from the token

            System.out.println("Extracted role from token: " + role); // Debug statement

            if (person != null && role != null) {
                // Create a list of authorities based on the role
                List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_" + role.toUpperCase()));
                System.out.println("Setting authorities: " + authorities); // Debug statement

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(person, null, authorities);
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } else {
                System.out.println("Person or role is null. Skipping authentication.");
            }
        } else {
            System.out.println("Authorization header missing or does not start with Bearer.");
        }

        // Continue the filter chain
        filterChain.doFilter(request, response);

    }
}
