package com.alphasolutions.piauieventos.service.message;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Service;

@Service
public interface MessageService {
    void processMessageUpsert(JsonNode payload);
}
