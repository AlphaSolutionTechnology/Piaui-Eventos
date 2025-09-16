package com.alphasolutions.piauieventos.dto;

public record LoginResponseDTO(
        String accessToken,
        String refreshToken
) {}
