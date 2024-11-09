package com.backend.auth;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtil {

    // Ensure the secret key is exactly 32 characters long
    private static final String SECRET_KEY = "yoursecretkeyyoursecretkeyyyyyyy"; // Update to a secure 32-character key
    private static final long EXPIRATION_TIME = 86400000; // 24 hours in milliseconds

    // Generate a token using the user ID and role
    public String generateToken(Long userId, String role) {  // Add 'role' parameter
        SecretKey key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());

        return Jwts.builder()
                .setSubject(String.valueOf(userId)) // Use the user ID as the subject
                .claim("role", role) // Include the role as a claim
                .setIssuedAt(new Date()) // Current time
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME)) // Token expiration (24 hours)
                .signWith(key, SignatureAlgorithm.HS256) // Sign the token with HMAC SHA-256
                .compact(); // Return the compact JWT token
    }

    // Validate the token and extract the user ID
    public static Long validateToken(String token) {
        try {
            SecretKey key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());

            // Parse the token
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            // Return the user ID (subject)
            return Long.parseLong(claims.getSubject());
        } catch (JwtException | IllegalArgumentException e) {
            // Token is invalid
            return null;
        }
    }
    public static Claims getClaimsFromToken(String token) {
        try {
            SecretKey key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());

            // Parse the token and return the claims
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (JwtException | IllegalArgumentException e) {
            // Log or handle invalid token
            return null;
        }
    }
}
