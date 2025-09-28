package com.alphasolutions.piauieventos.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(LocationNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleLocationNotFoundException(LocationNotFoundException locationNotFoundException) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", locationNotFoundException.getMessage()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String,String>> handleIllegalArgumentException(IllegalArgumentException illegalArgumentException) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", illegalArgumentException.getMessage()));
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<Map<String,String>> handleAuthenticationException(AuthenticationException authenticationException) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", authenticationException.getMessage()));
    }

    @ExceptionHandler(WhatsappCommunicationException.class)
    public ResponseEntity<Map<String,String>> handleWhatsappCommunicationException(WhatsappCommunicationException whatsappCommunicationException) {
        return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(Map.of("error", whatsappCommunicationException.getMessage()));
    }
}
