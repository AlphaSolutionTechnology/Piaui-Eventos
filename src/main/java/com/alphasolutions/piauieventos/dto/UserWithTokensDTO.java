package com.alphasolutions.piauieventos.dto;

public record UserWithTokensDTO(
        Long id,
        String name,
        String email,
        String phoneNumber,
        String accessToken,
        String refreshToken
) {}
