package com.alphasolutions.piauieventos.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EventRequestDTO {
    String name;
    String description;
    String imageUrl;
    LocalDateTime eventDate;
    String eventType;
    Integer maxSubs;
    @JsonProperty("location")
    EventLocationDTO eventLocationDTO;

    public void print(){
        System.out.println("EventRequestDTO {");
        System.out.println("  name: " + name);
        System.out.println("  description: " + description);
        System.out.println("  imageUrl: " + imageUrl);
        System.out.println("  eventDate: " + eventDate);
        System.out.println("  eventType: " + eventType);
        System.out.println("  maxSubs: " + maxSubs);
        if (eventLocationDTO != null) {
            System.out.println("  eventLocationDTO: {");
            System.out.println("    id: " + eventLocationDTO.id());
            System.out.println("    placeName: " + eventLocationDTO.placeName());
            System.out.println("    latitude: " + eventLocationDTO.latitude());
            System.out.println("    longitude: " + eventLocationDTO.longitude());
            System.out.println("    fullAddress: " + eventLocationDTO.fullAddress());
            System.out.println("    zipCode: " + eventLocationDTO.zipCode());
            System.out.println("    category: " + eventLocationDTO.category());
            System.out.println("  }");
        } else {
            System.out.println("  eventLocationDTO: null");
        }
        System.out.println("}");
    }

}