package com.alphasolutions.piauieventos.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EventUpdateDTO {
    String name;
    String description;
    String imageUrl;
    LocalDateTime eventDate;
    String eventType;
    Integer maxSubs;
    @JsonProperty("location")
    EventLocationDTO eventLocationDTO;
}

