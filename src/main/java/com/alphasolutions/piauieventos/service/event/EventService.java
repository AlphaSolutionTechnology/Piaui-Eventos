package com.alphasolutions.piauieventos.service.event;

import com.alphasolutions.piauieventos.dto.EventRequestDTO;
import com.alphasolutions.piauieventos.dto.EventResponseDTO;
import com.alphasolutions.piauieventos.dto.UserRegistrationDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface EventService {
    EventResponseDTO create(EventRequestDTO dto);
    EventResponseDTO update(Long id, EventRequestDTO dto);
    void delete(Long id);
    Page<EventResponseDTO> listEvents(Pageable pageable);
    EventResponseDTO findById(Long id);
    Page<EventResponseDTO> findByUserId(Long userId, Pageable pageable);
    void registerUser(Long eventId, UserRegistrationDTO registrationDTO);
    void unregisterUser(Long eventId, Long userId);
    Page<EventResponseDTO> findSubscribedEventsByUserId(Long userId, Pageable pageable);
}
