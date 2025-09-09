package com.alphasolutions.piauieventos.mapper;

import com.alphasolutions.piauieventos.dto.EventRequestDTO;
import com.alphasolutions.piauieventos.dto.EventResponseDTO;
import com.alphasolutions.piauieventos.dto.EventUpdateDTO;
import com.alphasolutions.piauieventos.model.Event;
import com.alphasolutions.piauieventos.model.EventLocation;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface EventMapper {

    @Mapping(target = "location", source = "location")
    Event toEntity(EventRequestDTO dto, EventLocation location);

    @Mapping(target = "locationId", source = "location.id")
    EventResponseDTO toDTO(Event event);

    List<EventResponseDTO> toDTO(List<Event> events);

    void updateEntityFromDto(EventUpdateDTO dto, @MappingTarget Event entity);
}
