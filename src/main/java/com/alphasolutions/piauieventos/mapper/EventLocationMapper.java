package com.alphasolutions.piauieventos.mapper;

import com.alphasolutions.piauieventos.dto.EventLocationDTO;
import com.alphasolutions.piauieventos.model.EventLocation;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EventLocationMapper {

    EventLocation eventLocationDtoToEventLocation(EventLocationDTO eventLocationDTO);
    EventLocationDTO eventLocationToEventLocation(EventLocation eventLocation);
}
