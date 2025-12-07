package com.example.gym.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import jakarta.servlet.http.HttpServletRequest;
import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> validation(MethodArgumentNotValidException e,
                                                          HttpServletRequest req) {
        Map<String, Object> details = new HashMap<>();
        e.getBindingResult().getFieldErrors()
                .forEach(fe -> details.put(fe.getField(), fe.getDefaultMessage()));

        return ResponseEntity.badRequest().body(Map.of(
                "timestamp", OffsetDateTime.now(),
                "status", 400,
                "error", "validation_failed",
                "path", req.getRequestURI(),
                "details", details
        ));
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Map<String, Object>> rse(ResponseStatusException e,
                                                   HttpServletRequest req) {
        return ResponseEntity.status(e.getStatusCode()).body(Map.of(
                "timestamp", OffsetDateTime.now(),
                "status", e.getStatusCode().value(),
                "error", e.getStatusCode().toString(),
                "message", e.getReason(),
                "path", req.getRequestURI()
        ));
    }
}
