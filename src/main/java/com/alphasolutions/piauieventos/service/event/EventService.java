package com.alphasolutions.piauieventos.service.event;

import com.alphasolutions.piauieventos.dto.EventRequestDTO;
import com.alphasolutions.piauieventos.dto.EventResponseDTO;
import java.util.List;

public interface EventService {
    EventResponseDTO create(EventRequestDTO dto);
    EventResponseDTO update(Long id, EventRequestDTO dto);
    void delete(Long id);
    List<EventResponseDTO> listEvents();
    EventResponseDTO findById(Long id);
}
