package com.alphasolutions.piauieventos.service;

import com.alphasolutions.piauieventos.dto.EventRequestDTO;
import com.alphasolutions.piauieventos.dto.EventResponseDTO;

public interface EventService {
    EventResponseDTO create(EventRequestDTO dto);
    EventResponseDTO update(EventResponseDTO dto);
}


