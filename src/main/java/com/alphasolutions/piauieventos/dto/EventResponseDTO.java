package com.alphasolutions.piauieventos.dto;

import com.alphasolutions.piauieventos.model.Event;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class EventResponseDTO {
    Long id;
    String name;
    String description;
    String imageUrl;
    LocalDateTime eventDate;
    String eventType;
    Integer maxSubs;
    Long locationId;

    public EventResponseDTO(Event event) {
        this.id          = event.getId();
        this.name        = event.getName();
        this.description = event.getDescription();
        this.imageUrl    = event.getImageUrl();
        this.eventDate   = event.getEventDate();
        this.eventType   = event.getEventType();
        this.maxSubs     = event.getMaxSubs();
        this.locationId  = event.getLocation().getId();

    }
}
