package com.alphasolutions.piauieventos.dto;

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
    EventLocationDTO eventLocation;
    Integer version;
    Integer subscribersCount;

}
