package com.alphasolutions.piauieventos.controller;

import com.alphasolutions.piauieventos.dto.LoginRequestDTO;
import com.alphasolutions.piauieventos.dto.LoginResponseDTO;
import com.alphasolutions.piauieventos.dto.RefreshRequestDTO;
import com.alphasolutions.piauieventos.dto.RefreshResponseDTO;
import com.alphasolutions.piauieventos.service.auth.AuthService;
import com.alphasolutions.piauieventos.service.auth.TokenHttpService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final TokenHttpService tokenHttpService;

    public AuthController(AuthService authService, TokenHttpService tokenHttpService) {
        this.authService = authService;
        this.tokenHttpService = tokenHttpService;
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody LoginRequestDTO request, HttpServletResponse response) {
        LoginResponseDTO loginResponse = authService.login(request);
        tokenHttpService.writeAuthCookies(response, loginResponse.accessToken(), loginResponse.refreshToken());
        tokenHttpService.writeAuthHeader(response, loginResponse.accessToken());
        return ResponseEntity.ok(Map.of(
                "message", "Login realizado com sucesso",
                "accessToken", loginResponse.accessToken()
        ));
    }

    @PostMapping("/refresh")
    public ResponseEntity<Map<String, String>> refresh(@CookieValue(value = "refreshToken", required = false) String refreshToken,
                                                       HttpServletResponse response) {
        if (refreshToken == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "Refresh token não encontrado"));
        }
        RefreshResponseDTO refreshResponse = authService.refresh(new RefreshRequestDTO(refreshToken));
        tokenHttpService.writeAuthCookies(response, refreshResponse.accessToken(), refreshResponse.refreshToken());
        tokenHttpService.writeAuthHeader(response, refreshResponse.accessToken());
        return ResponseEntity.ok(Map.of(
                "message", "Tokens renovados com sucesso",
                "accessToken", refreshResponse.accessToken()
        ));
    }

    @PostMapping("/logout")
    public ResponseEntity<Map<String, String>> logout(HttpServletResponse response) {
        tokenHttpService.clearAuthCookies(response);
        return ResponseEntity.ok(Map.of("message", "Logout realizado com sucesso"));
    }
}
