package com.alphasolutions.piauieventos.service;

import com.alphasolutions.piauieventos.dto.EventLocationDTO;
import com.alphasolutions.piauieventos.mapper.EventLocationMapper;
import com.alphasolutions.piauieventos.model.EventLocation;
import com.alphasolutions.piauieventos.model.LocationCategory;
import com.alphasolutions.piauieventos.repository.EventLocationRepository;
import com.alphasolutions.piauieventos.repository.LocationCategoryRepository;
import org.springframework.stereotype.Service;

@Service
public class EventLocationServiceImpl implements EventLocationService {
    private final EventLocationRepository eventLocationRepository;
    private final EventLocationMapper eventLocationMapper;
    private final LocationCategoryRepository locationCategoryRepository;

    public EventLocationServiceImpl(EventLocationRepository eventLocationRepository, EventLocationMapper eventLocationMapper, LocationCategoryRepository locationCategoryRepository) {
        this.eventLocationRepository = eventLocationRepository;
        this.eventLocationMapper = eventLocationMapper;
        this.locationCategoryRepository = locationCategoryRepository;
    }


    @Override
    public EventLocation addLocation(EventLocationDTO eventLocationDTO) {
        EventLocation eventLocation = eventLocationMapper.eventLocationDtoToEventLocation(eventLocationDTO);
        if (eventLocationDTO.category() != null && !eventLocationDTO.category().isBlank()) {
            LocationCategory category = locationCategoryRepository.findByCategoryName(eventLocationDTO.category());
            if (category == null) {
                category = new LocationCategory();
                category.setCategoryName(eventLocationDTO.category());
                System.out.println(category);
                locationCategoryRepository.save(category);
            }

            eventLocation.setCategoryId(category);
        }
        return eventLocationRepository.save(eventLocation);
    }

}
