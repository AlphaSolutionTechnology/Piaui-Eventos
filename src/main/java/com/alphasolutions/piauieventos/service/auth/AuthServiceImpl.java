package com.alphasolutions.piauieventos.service.auth;

import com.alphasolutions.piauieventos.dto.LoginRequestDTO;
import com.alphasolutions.piauieventos.dto.LoginResponseDTO;
import com.alphasolutions.piauieventos.dto.RefreshRequestDTO;
import com.alphasolutions.piauieventos.dto.RefreshResponseDTO;
import com.alphasolutions.piauieventos.util.JwtUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public AuthServiceImpl(AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public LoginResponseDTO login(LoginRequestDTO request) {
        if (request.username() == null || request.username().isBlank()) {
            throw new IllegalArgumentException("Username é obrigatório");
        }
        if (request.password() == null || request.password().isBlank()) {
            throw new IllegalArgumentException("Password é obrigatório");
        }

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.username(), request.password())
        );

        String accessToken = jwtUtil.generateToken(request.username());
        String refreshToken = jwtUtil.generateRefreshToken(request.username());

        return new LoginResponseDTO(accessToken, refreshToken);
    }

    @Override
    public RefreshResponseDTO refresh(RefreshRequestDTO request) {
        if (request.refreshToken() == null || request.refreshToken().isBlank()) {
            throw new IllegalArgumentException("Refresh token é obrigatório");
        }

        if (!jwtUtil.validateToken(request.refreshToken())) {
            throw new IllegalArgumentException("Refresh token expirado");
        }

        if (!jwtUtil.isRefreshToken(request.refreshToken())) {
            throw new IllegalArgumentException("Token fornecido não é um refresh token");
        }

        String username = jwtUtil.getUsernameFromToken(request.refreshToken());
        String newAccessToken = jwtUtil.generateToken(username);
        String newRefreshToken = jwtUtil.generateRefreshToken(username);

        return new RefreshResponseDTO(newAccessToken, newRefreshToken);
    }
}
