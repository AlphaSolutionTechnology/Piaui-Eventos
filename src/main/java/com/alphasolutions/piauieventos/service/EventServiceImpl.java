package com.alphasolutions.piauieventos.service;

import com.alphasolutions.piauieventos.dto.EventRequestDTO;
import com.alphasolutions.piauieventos.dto.EventResponseDTO;
import com.alphasolutions.piauieventos.exception.EventNotFoundException;
import com.alphasolutions.piauieventos.exception.LocationNotFoundException;
import com.alphasolutions.piauieventos.mapper.EventMapper;
import com.alphasolutions.piauieventos.model.Event;
import com.alphasolutions.piauieventos.model.EventLocation;
import com.alphasolutions.piauieventos.repository.EventRepository;
import com.alphasolutions.piauieventos.repository.EventLocationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final EventLocationRepository eventLocationRepository;
    private final EventMapper eventMapper;
    private final EventLocationService eventLocationService;

    public EventServiceImpl(EventRepository eventRepository,
                            EventLocationRepository eventLocationRepository,
                            EventMapper eventMapper, EventLocationService eventLocationService) {
        this.eventRepository    = eventRepository;
        this.eventLocationRepository = eventLocationRepository;
        this.eventMapper        = eventMapper;
        this.eventLocationService = eventLocationService;
    }

    @Override
    public EventResponseDTO create(EventRequestDTO dto) {
        EventLocation eventLocation = eventLocationService.addLocation(dto.getEventLocation());
        Event event = eventMapper.toEntity(dto, eventLocation);

        Event savedEvent = eventRepository.save(event);

        return eventMapper.toDTO(savedEvent);
    }

    @Override
    public void delete(Long id) {

        if (!eventRepository.existsById(id)) {
            throw new EventNotFoundException("Event not found with id: " + id);
        }

        eventRepository.deleteById(id);
    }

    @Override
    public List<EventResponseDTO> listEvents() {
        // Get all events
        List<Event> events = eventRepository.findAll();

        return eventMapper.toDTO(events);
    }

    @Override
    public EventResponseDTO update(Long id, EventRequestDTO dto) {
        Event existing = eventRepository.findById(id)
                .orElseThrow(() -> new EventNotFoundException("Event not found with id: " + id));

        EventLocation location = eventLocationRepository.findById(dto.getEventLocation().getId())
                .orElseThrow(() -> new LocationNotFoundException("Location not found with id: " + dto.getEventLocation().getId())) ;

        existing.setName(dto.getName());
        existing.setDescription(dto.getDescription());
        existing.setImageUrl(dto.getImageUrl());
        existing.setEventDate(dto.getEventDate());
        existing.setEventType(dto.getEventType());
        existing.setMaxSubs(dto.getMaxSubs());
        existing.setLocation(location);

        Event saved = eventRepository.save(existing);
        return eventMapper.toDTO(saved);
    }
}
