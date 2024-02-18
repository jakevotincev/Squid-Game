package ru.jakev.backend.services;

import org.springframework.security.core.userdetails.UserDetails;

/**
 * @author evotintsev
 * @since 18.02.2024
 */
public interface JwtService {
    String generateToken(UserDetails userDetails);
    boolean isTokenValid(String token, UserDetails userDetails);
    String extractUserName(String token);
}
