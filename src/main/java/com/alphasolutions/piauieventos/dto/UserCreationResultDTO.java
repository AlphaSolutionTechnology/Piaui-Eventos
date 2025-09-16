package com.alphasolutions.piauieventos.dto;

public record UserCreationResultDTO(
        UserCreatedResponseDTO userData,
        String accessToken,
        String refreshToken
) {}
