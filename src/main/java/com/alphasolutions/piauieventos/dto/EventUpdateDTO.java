package com.alphasolutions.piauieventos.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class EventUpdateDTO {
    String name;
    String description;
    String imageUrl;
    LocalDateTime eventDate;
    String eventType;
    Integer maxSubs;
    Long locationId;
}
