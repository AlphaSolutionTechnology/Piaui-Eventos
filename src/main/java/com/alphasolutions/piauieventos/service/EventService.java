package com.alphasolutions.piauieventos.service;

import com.alphasolutions.piauieventos.dto.EventRequestDTO;
import com.alphasolutions.piauieventos.dto.EventResponseDTO;
import java.util.List;

public interface EventService {
    // Create event
    EventResponseDTO create(EventRequestDTO dto);

    // Delete event
    void delete(Long id);

    // List all events
    List<EventResponseDTO> listEvents();
}


