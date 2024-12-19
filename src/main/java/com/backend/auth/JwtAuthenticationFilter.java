package com.backend.auth;

import com.backend.entity.Person.Person;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
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
            System.out.println("Extracted person from token: "+ person.getId());
            System.out.println("Extracted role from token: " + role); // Debug statement

            if (person != null && role != null) {
                // Set the authority based on the extracted role without the "ROLE_" prefix
                List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(role.toLowerCase()));
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
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        // List of public endpoints to exclude from the filter
        return path.equals("/auth/verify-key") || path.equals("/auth/reset-password");
    }

}
