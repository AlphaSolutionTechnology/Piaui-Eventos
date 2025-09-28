package com.alphasolutions.piauieventos.mapper;

import com.alphasolutions.piauieventos.dto.EventRequestDTO;
import com.alphasolutions.piauieventos.dto.EventResponseDTO;
import com.alphasolutions.piauieventos.model.Event;
import com.alphasolutions.piauieventos.model.EventLocation;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", uses = {EventLocationMapper.class})
public interface EventMapper {

    @Mapping(target = "location", source = "location")
    Event toEntity(EventRequestDTO dto, EventLocation location);

    @Mapping(target = "eventLocation", source = "location")
    EventResponseDTO toDTO(Event event);

    List<EventResponseDTO> toDTO(List<Event> events);
}
