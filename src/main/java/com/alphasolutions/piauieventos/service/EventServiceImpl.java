package com.alphasolutions.piauieventos.service;

import com.alphasolutions.piauieventos.dto.EventRequestDTO;
import com.alphasolutions.piauieventos.dto.EventResponseDTO;
import com.alphasolutions.piauieventos.mapper.EventMapper;
import com.alphasolutions.piauieventos.model.Event;
import com.alphasolutions.piauieventos.model.EventLocation;
import com.alphasolutions.piauieventos.repository.EventRepository;
import com.alphasolutions.piauieventos.repository.LocationRepository;
import org.springframework.stereotype.Service;

@Service
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final LocationRepository locationRepository;
    private final EventMapper eventMapper;

    public EventServiceImpl(EventRepository eventRepository,
                            LocationRepository locationRepository,
                            EventMapper eventMapper) {
        this.eventRepository    = eventRepository;
        this.locationRepository = locationRepository;
        this.eventMapper        = eventMapper;
    }

    @Override
    public EventResponseDTO create(EventRequestDTO dto) {

        // Get Event Location
        EventLocation location = locationRepository.findById(dto.getLocationId())
                .orElseThrow(() -> new RuntimeException("Location not found"));

        // Create event
        Event event = eventMapper.toEntity(dto, location);

        // save event
        Event savedEvent = eventRepository.save(event);

        // convert to DTO Response
        return eventMapper.toDTO(savedEvent);
    }

    @Override
    public void delete(Long id) {

        // Verify if the event exists
        if (!eventRepository.existsById(id)) {
            throw new RuntimeException("Event not found");
        }

        // Delete event
        eventRepository.deleteById(id);
    }

}
