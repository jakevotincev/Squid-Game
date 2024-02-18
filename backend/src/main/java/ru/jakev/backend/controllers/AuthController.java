package ru.jakev.backend.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.jakev.backend.dto.AuthRequestDTO;
import ru.jakev.backend.dto.AuthResponseDTO;
import ru.jakev.backend.services.AuthService;

/**
 * @author evotintsev
 * @since 18.02.2024
 */
@RestController
@CrossOrigin
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody AuthRequestDTO authRequestDTO) {
        boolean registered = authService.register(authRequestDTO);
        //todo: добавить ли тут генерацию токена?
        //todo: нормально обрабатывать ошибки
        return registered ? ResponseEntity.ok("Account created") :
                ResponseEntity.badRequest().body("Error occurred while registration");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequestDTO authRequestDTO) {
        String token = authService.login(authRequestDTO);
        //todo: add return auth response dto
        return token != null ? ResponseEntity.ok(new AuthResponseDTO(token)) :
                ResponseEntity.badRequest().body("Incorrect login or password");
    }
}
