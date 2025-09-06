package com.alphasolutions.piauieventos.dto;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class EventRequestDTO {
    String name;
    String description;
    String imageUrl;
    LocalDateTime eventDate;
    String eventType;
    Integer maxSubs;
    Long locationId;
}