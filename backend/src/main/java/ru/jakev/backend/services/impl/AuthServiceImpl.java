package ru.jakev.backend.services.impl;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.jakev.backend.dto.AccountDTO;
import ru.jakev.backend.dto.AuthRequestDTO;
import ru.jakev.backend.entities.Role;
import ru.jakev.backend.services.AccountService;
import ru.jakev.backend.services.AuthService;
import ru.jakev.backend.services.JwtService;

/**
 * @author evotintsev
 * @since 18.02.2024
 */
@Service
public class AuthServiceImpl implements AuthService {

    private final AccountService accountService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthServiceImpl(AccountService accountService, PasswordEncoder passwordEncoder,
                           AuthenticationManager authenticationManager, JwtService jwtService) {
        this.accountService = accountService;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @Override
    public boolean register(AuthRequestDTO authRequestDTO) {
        AccountDTO account = new AccountDTO();
        account.setUsername(authRequestDTO.getUsername());
        account.setPassword(passwordEncoder.encode(authRequestDTO.getPassword()));
        account.setRole(Role.UNDEFINED);

        return accountService.saveAccount(account);
    }

    @Override
    public String login(AuthRequestDTO authRequestDTO) {
        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequestDTO.getUsername(), authRequestDTO.getPassword()));
        } catch (AuthenticationException e) {
            e.printStackTrace();
            return null;
        }

        return jwtService.generateToken((UserDetails) authentication.getPrincipal());
    }
}
