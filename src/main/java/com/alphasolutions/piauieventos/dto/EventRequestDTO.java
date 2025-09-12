package com.alphasolutions.piauieventos.dto;

import java.time.LocalDateTime;

import com.alphasolutions.piauieventos.model.EventLocation;

public record EventRequestDTO (
        String name,
        String description,
        String imageUrl,
        LocalDateTime eventDate,
        String eventType,
        Integer maxSubs,
        EventLocation eventLocation
) {}