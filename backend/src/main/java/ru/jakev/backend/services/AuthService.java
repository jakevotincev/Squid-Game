package ru.jakev.backend.services;

import ru.jakev.backend.dto.AuthRequestDTO;

/**
 * @author evotintsev
 * @since 18.02.2024
 */
public interface AuthService {
    boolean register(AuthRequestDTO authRequestDTO);

    String login(AuthRequestDTO authRequestDTO);
}
