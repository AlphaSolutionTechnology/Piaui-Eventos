package com.alphasolutions.piauieventos.dto;

public record UserResponseDTO (
        Long id,
        String name,
        String email,
        String phoneNumber
) {
    public record RoleDTO (
            Integer roleId,
            String roleName
    ) {}
}

