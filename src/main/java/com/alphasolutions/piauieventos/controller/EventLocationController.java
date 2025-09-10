package com.alphasolutions.piauieventos.controller;

import com.alphasolutions.piauieventos.dto.ViaCepResponse;
import com.alphasolutions.piauieventos.service.ViaCepService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/location")
public class EventLocationController {

    private final ViaCepService viaCepService;

    public EventLocationController(ViaCepService viaCepService) {
        this.viaCepService = viaCepService;
    }

    @GetMapping("/{cep}")
    public ResponseEntity<?> addAddress(@PathVariable String cep) {
        ViaCepResponse viaCepResponse = viaCepService.getAddressByCep(cep);
        return ResponseEntity.ok(viaCepResponse);
    }
}
