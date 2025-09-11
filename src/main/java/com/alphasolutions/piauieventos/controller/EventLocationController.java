package com.alphasolutions.piauieventos.controller;

import com.alphasolutions.piauieventos.dto.EventLocationDTO;
import com.alphasolutions.piauieventos.dto.ViaCepResponse;
import com.alphasolutions.piauieventos.mapper.EventLocationMapper;
import com.alphasolutions.piauieventos.model.EventLocation;
import com.alphasolutions.piauieventos.service.EventLocationService;
import com.alphasolutions.piauieventos.service.ViaCepService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/location")
public class EventLocationController {

    private final ViaCepService viaCepService;
    private final EventLocationService eventLocationService;
    private final EventLocationMapper eventLocationMapper;

    public EventLocationController(ViaCepService viaCepService, EventLocationService eventLocationService, EventLocationMapper eventLocationMapper) {
        this.viaCepService = viaCepService;
        this.eventLocationService = eventLocationService;
        this.eventLocationMapper = eventLocationMapper;
    }

    @GetMapping(value = "/{cep}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addAddress(@PathVariable String cep) {
        ViaCepResponse viaCepResponse = viaCepService.getAddressByCep(cep);
        return ResponseEntity.ok(viaCepResponse);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<EventLocationDTO> createLocation(@RequestBody EventLocationDTO dto) {
        EventLocation saved = eventLocationService.addLocation(dto);
        EventLocationDTO response = eventLocationMapper.eventLocationToEventLocation(saved);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
