package com.alphasolutions.piauieventos.controller;

import com.alphasolutions.piauieventos.service.message.MessageService;
import com.fasterxml.jackson.databind.JsonNode;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/whatsapp")
public class WhatsAppController {


    private final MessageService messageService;

    public WhatsAppController(MessageService messageService) {
        this.messageService = messageService;
    }

    @PostMapping("/messages-upsert")
    public ResponseEntity<String> messagesUpsert(@RequestBody JsonNode payload) {
        messageService.processMessageUpsert(payload);
        return  ResponseEntity.ok("Evento recebido com sucesso");
    }

    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Webhook WhatsApp funcionando - Eventos separados");
    }
}
