package com.alphasolutions.piauieventos.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class EventResponseDTO {
    Long id;
    String name;
    String description;
    String imageUrl;

    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    LocalDateTime eventDate;

    String eventType;
    Integer maxSubs;
    EventLocationDTO location;
    Integer version;
    Integer subscribedCount;
}
