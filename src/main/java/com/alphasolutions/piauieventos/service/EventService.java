package com.alphasolutions.piauieventos.service;

import com.alphasolutions.piauieventos.dto.EventRequestDTO;
import com.alphasolutions.piauieventos.dto.EventResponseDTO;

public interface EventService {
    // Create event
    EventResponseDTO create(EventRequestDTO dto);

    // Delete event
    void delete(Long id);
}


