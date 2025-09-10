package com.alphasolutions.piauieventos.service;

import com.alphasolutions.piauieventos.mapper.EventLocationMapper;
import com.alphasolutions.piauieventos.model.EventLocation;
import com.alphasolutions.piauieventos.repository.EventLocationRepository;
import org.springframework.stereotype.Service;

@Service
public class EventLocationServiceImpl implements EventLocationService {
    private final EventLocationRepository eventLocationRepository;
    private final ViaCepService viaCepService;

    public EventLocationServiceImpl(EventLocationRepository eventLocationRepository, EventLocationMapper eventLocationMapper, ViaCepService viaCepService) {
        this.eventLocationRepository = eventLocationRepository;
        this.viaCepService = viaCepService;
    }


    @Override
    public EventLocation addLocation(EventLocation eventLocation) {
        eventLocationRepository.save(eventLocation);
        return eventLocation;
    }

}
