package com.alphasolutions.piauieventos.dto;

public record RefreshResponseDTO(
        String accessToken,
        String refreshToken
) {}
