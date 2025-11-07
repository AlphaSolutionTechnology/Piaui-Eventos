package com.alphasolutions.piauieventos.controller;

import com.alphasolutions.piauieventos.dto.EventRequestDTO;
import com.alphasolutions.piauieventos.dto.EventResponseDTO;
import com.alphasolutions.piauieventos.dto.UserRegistrationDTO;
import com.alphasolutions.piauieventos.service.event.EventService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/events")
public class EventController {
    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @PostMapping
    public ResponseEntity<EventResponseDTO> createEvent(@RequestBody EventRequestDTO dto) {
        EventResponseDTO response = eventService.create(dto);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long id) {
        eventService.delete(id);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping
    public ResponseEntity<Page<EventResponseDTO>> listEvents(
            @PageableDefault(size = 10, sort = "eventDate") Pageable pageable) {
        Page<EventResponseDTO> events = eventService.listEvents(pageable);

        return ResponseEntity.ok(events);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EventResponseDTO> updateEvent(@PathVariable Long id, @RequestBody EventRequestDTO dto) {
        EventResponseDTO response = eventService.update(id, dto);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventResponseDTO> findEventById(@PathVariable Long id) {
        EventResponseDTO response = eventService.findById(id);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{eventId}/register")
    public ResponseEntity<Void> registerUser(
            @PathVariable Long eventId,
            @RequestBody UserRegistrationDTO registrationDTO) {
        eventService.registerUser(eventId, registrationDTO);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<Page<EventResponseDTO>> eventsCreatedByUser(
            @PathVariable Long userId,
            @PageableDefault(size = 10, sort = "eventDate") Pageable pageable) {
        Page<EventResponseDTO> response = eventService.findByUserId(userId, pageable);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/subscribed/user/{userId}")
    public ResponseEntity<Page<EventResponseDTO>> eventsSubscribedByUser(
            @PathVariable Long userId,
            @PageableDefault(size = 10, sort = "eventDate") Pageable pageable){
        Page<EventResponseDTO> response = eventService.findSubscribedEventsByUserId(userId, pageable);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{eventId}/register/{userId}")
    public ResponseEntity<Void> unregisterUser(
            @PathVariable Long eventId,
            @PathVariable Long userId) {
        eventService.unregisterUser(eventId, userId);
        return ResponseEntity.noContent().build();
    }
}
