package com.alphasolutions.piauieventos.dto;

public record UserRequestDTO (
        Long id,
        String name,
        String email,
        String password,
        String phoneNumber,
        Long roleId
) {}
