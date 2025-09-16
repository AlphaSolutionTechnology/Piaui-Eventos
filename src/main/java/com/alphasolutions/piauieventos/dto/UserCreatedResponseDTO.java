package com.alphasolutions.piauieventos.dto;

public record UserCreatedResponseDTO(
        Long id,
        String name,
        String email,
        String phoneNumber
) {}
