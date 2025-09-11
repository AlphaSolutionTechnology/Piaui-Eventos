package com.alphasolutions.piauieventos.mapper;

import com.alphasolutions.piauieventos.dto.EventLocationDTO;
import com.alphasolutions.piauieventos.model.EventLocation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface EventLocationMapper {

    @Mapping(target = "categoryId", ignore = true)
    EventLocation eventLocationDtoToEventLocation(EventLocationDTO eventLocationDTO);

    @Mapping(target = "category", source = "categoryId.categoryName")
    EventLocationDTO eventLocationToEventLocation(EventLocation eventLocation);
}
