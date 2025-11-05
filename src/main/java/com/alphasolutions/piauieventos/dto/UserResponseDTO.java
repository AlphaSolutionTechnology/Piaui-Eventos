package com.alphasolutions.piauieventos.dto;

public record UserResponseDTO (
        Long id,
        String name,
        String email,
        String phoneNumber,
        RoleDTO role
) {}

