package com.alphasolutions.piauieventos.service.event;

import com.alphasolutions.piauieventos.dto.EventRequestDTO;
import com.alphasolutions.piauieventos.dto.EventResponseDTO;
import com.alphasolutions.piauieventos.dto.UserRegistrationDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface EventService {
    EventResponseDTO create(EventRequestDTO dto);
    EventResponseDTO update(Long id, EventRequestDTO dto);
    void delete(Long id);
    Page<EventResponseDTO> listEvents(Pageable pageable);
    EventResponseDTO findById(Long id);
    void registerUser(Long eventId, UserRegistrationDTO registrationDTO);
    void unregisterUser(Long eventId, Long userId);

    List<EventResponseDTO> upcomingEvents();
    List<EventResponseDTO> nextWeekEvents();
    List<EventResponseDTO> nextMonthEvents();
    List<EventResponseDTO> mostSubscribedEvents();
    void feedEvent();
}
