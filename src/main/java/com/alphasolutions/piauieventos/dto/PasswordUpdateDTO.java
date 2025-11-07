package com.alphasolutions.piauieventos.dto;

public record PasswordUpdateDTO(
        String currentPassword,
        String newPassword
) {}

