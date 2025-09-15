package com.alphasolutions.piauieventos.controller;

import com.alphasolutions.piauieventos.dto.EventRequestDTO;
import com.alphasolutions.piauieventos.dto.EventResponseDTO;
import com.alphasolutions.piauieventos.service.EventService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/events")
public class EventController {
    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<EventResponseDTO> createEvent(@RequestBody EventRequestDTO dto) {
        EventResponseDTO response = eventService.create(dto);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping(value = "/{id}", consumes =  MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> deleteEvent(@PathVariable Long id) {
        eventService.delete(id);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<EventResponseDTO>> listEvents() {
        List<EventResponseDTO> events = eventService.listEvents();

        return ResponseEntity.ok(events);
    }

    @PutMapping(value = "/{id}",consumes =  MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<EventResponseDTO> updateEvent(@PathVariable Long id, @RequestBody EventRequestDTO dto) {
        EventResponseDTO response = eventService.update(id, dto);
        return ResponseEntity.ok(response);
    }
}
