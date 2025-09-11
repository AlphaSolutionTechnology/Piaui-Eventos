package com.alphasolutions.piauieventos.dto;

import java.time.LocalDateTime;

public record EventResponseDTO(
        Long id,
        String name,
        String description,
        String imageUrl,
        LocalDateTime eventDate,
        String eventType,
        Integer maxSubs,
        Long locationId
) {}
