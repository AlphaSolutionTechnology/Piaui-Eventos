package com.alphasolutions.piauieventos.exception;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(LocationNotFoundException.class)
    public String handleLocationNotFoundException(LocationNotFoundException locationNotFoundException) {
        return locationNotFoundException.getMessage();
    }

}
